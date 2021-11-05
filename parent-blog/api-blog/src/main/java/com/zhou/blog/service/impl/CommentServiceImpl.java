package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhou.blog.dao.mapper.ArticleMapper;
import com.zhou.blog.dao.mapper.CommentMapper;
import com.zhou.blog.dao.pojo.Comment;
import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.service.CommentService;
import com.zhou.blog.service.SysUserService;
import com.zhou.blog.utils.UserThreadLocal;
import com.zhou.blog.vo.params.CommentParam;
import com.zhou.blog.vo.CommentVo;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {



    @Resource
    private  ArticleMapper articleMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Result commentsByArticleId(Long id) {
        /*
        *   根据文章id 查询评论列表
        *   根据作者id 查询 作者 头像 nickname id
        *   如果level = 1 要查询有没有子评论
        *   如果有 根据评论id 查询
        *
        * */

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Comment::getArticleId, id);

        lambdaQueryWrapper.eq(Comment::getLevel, 1);

        List<Comment> comments = commentMapper.selectList(lambdaQueryWrapper);

        List<CommentVo> commentVoList = copyList(comments);




        return Result.success(commentVoList);
    }


    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return  commentVoList;
    }


    @Autowired
    private  SysUserService sysUserService;
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setId(String.valueOf(comment.getId()));

        // 作者信息
        Long authorId = comment.getAuthorId();
//        sysUserService.
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        // 子评论 level 1
        Integer level = comment.getLevel();
        if(1 == level) {
              Long id = comment.getId();
              List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }

        //给谁评论
        if(level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }



        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id).eq(Comment::getLevel, 2);

        return copyList(commentMapper.selectList(queryWrapper));
    }



    /*
     *  评论
     *
     * */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();

        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if(parent == null || parent == 0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }






}
