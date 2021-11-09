<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="pageParams.queryString" placeholder="权限名称" style="width: 200px;" class="filter-item" />
      <el-button class="dalfBut" @click="findSearchPage()">查询</el-button>
      <el-button type="primary" class="butT" @click="handleCreate()">新建权限</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="序号" prop="id" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="路径" align="center" prop="path" />
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagiantion"
      :current-page="pageParams.currentPage"
      :page-size="pageParams.pageSize"
      layout="total, prev, pager, next, jumper"
      :total="pageParams.total"
      @current-change="handleCurrentChange"
    />
    <el-dialog title="新增" :visible.sync="dialogFormVisible">
      <el-form ref="dataAddForm" :model="formData" :rules="rules" label-position="right" label-width="100px">
        <el-row>
          <el-col>
            <el-form-item label="名称" prop="name">
              <el-input v-model="formData.name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="路径" prop="path">
              <el-input v-model="formData.path" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="描述" prop="description">
              <el-input v-model="formData.description" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd()">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑" :visible.sync="dialogFormVisibleEdit">
      <el-form ref="dataAddForm" :model="formData" :rules="rules" label-position="right" label-width="100px">
        <el-row>
          <el-col>
            <el-form-item label="名称" prop="name">
              <el-input v-model="formData.name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="路径" prop="path">
              <el-input v-model="formData.path" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="描述" prop="description">
              <el-input v-model="formData.description" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEdit = false">取消</el-button>
        <el-button type="primary" @click="handleEdit()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPermissList, add, update, deleteById } from '@/api/table'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      dialogFormVisibleEdit: false,
      list: [],
      listLoading: true,
      formData: {
        path: '',
        description: '',
        name: ''
      },
      pageParams: {
        currentPage: 1,
        pageSize: 2,
        total: 0,
        queryString: null
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getPermissList(this.pageParams).then(res => {
        this.list = res.data.list
        this.pageParams.total = res.data.total
      }).finally(() => {
        this.listLoading = false
      })
    },
    // 切换页码
    handleCurrentChange(currentPage) {
      this.pageParams.currentPage = currentPage
      this.fetchData()
    },
    // 弹出添加窗口
    handleCreate() {
      this.resetForm()
      this.dialogFormVisible = true
    },
    findSearchPage() {
      this.pageParams.currentPage = 1
      this.fetchData()
    },
    // 重置表单
    resetForm() {
      this.formData = {}
      this.subjects = []
      this.tableData = {}
    },
    handleAdd() {
      add(this.formData).then((res) => {
        if (res.code === 200) {
          this.$message({
            message: '恭喜你，添加成功',
            type: 'success'
          })
          this.dialogFormVisible = false
          this.findSearchPage()
        }
      }).catch((error) => {
        if (error === 'Error: Request failed with status code 403') {
          this.$message.error('无操作权限')
        }
      })
    },
    // 弹出编辑窗口
    handleUpdate(row) {
      this.resetForm()
      this.dialogFormVisibleEdit = true
      this.formData = row
    },
    // 编辑
    handleEdit() {
      update(this.formData).then((res) => {
        if (res.code === 200) {
          this.$message({
            message: '恭喜你，编辑成功',
            type: 'success'
          })
          this.dialogFormVisibleEdit = false
          this.findSearchPage()
        }
      }).catch((error) => {
        if (error === 'Error: Request failed with status code 403') {
          this.$message.error('无操作权限')
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteById(row.id).then((res) => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.findSearchPage()
          }
        }).catch((error) => {
          if (error === 'Error: Request failed with status code 403') {
            this.$message.error('无操作权限')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>
