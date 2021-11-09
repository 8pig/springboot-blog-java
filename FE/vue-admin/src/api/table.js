import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/vue-admin-template/table/list',
    method: 'get',
    params
  })
}

export function getPermissList(data) {
  return request({
    url: '/permission/permissionList',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/permission/add',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/permission/update',
    method: 'post',
    data
  })
}

export function deleteById(id) {
  return request({
    url: `/permission/delete/${id}`,
    method: 'get'
  })
}
