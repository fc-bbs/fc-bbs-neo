package com.bbs.njtech.posting.service;


import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.WxAPI.Service.WxSaveService;
import com.bbs.njtech.WxAPI.domain.TextCheckResponse;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.message.domain.Message;
import com.bbs.njtech.message.repo.MessageRepo;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.SubComment;
import com.bbs.njtech.posting.param.CommentToCommentContentParam;
import com.bbs.njtech.posting.param.CommentToSubCommentContentParam;
import com.bbs.njtech.posting.param.SubCommentDeleteParam;
import com.bbs.njtech.posting.param.SubCommentParam;
import com.bbs.njtech.posting.repo.CommentRepo;
import com.bbs.njtech.posting.repo.PostMessageRepo;
import com.bbs.njtech.posting.repo.SubCommentRepo;
import com.bbs.njtech.posting.vo.SubCommentVO;
import com.bbs.njtech.user.domain.User;
import com.bbs.njtech.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
@Slf4j
public class SubCommentService {
//回复评论服务
    @Autowired
    private SubCommentRepo subCommentRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostMessageRepo postMessageRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private WxSaveService wxSaveService;

    @Autowired
    private UserRepo userRepo;
    @Value("${wx-text-check}")
    private String wxTextCheck;
    //用于根据commentId查找所有的子评论，
    @Transactional(readOnly = true)
    public List<SubCommentVO> findAllSubCommentByCommentId(SubCommentParam param){

        List<SubComment> subCommentList = subCommentRepo.
                findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(param.getCommentId(),false);

        return SubCommentVO.build(subCommentList);
    }
    @Transactional
    public void commentToComment(CommentToCommentContentParam param){//针对评论创建一条子评论
        Comment comment = commentRepo.getReferenceById(param.getCommentId());
        SubComment subcomment = SubComment.buildByCommentToComment(
                param.getCommentId(), param.getUserId(),comment.getUserId(), param.getContent());//新增一条评论记录

        User user = userRepo.getReferenceById(param.getUserId());
        if(user.getDeleteFlag()){//该用户已经被封禁
            throw new BizException("您已被封禁");
        }
        if(wxTextCheck.equals("true")){
            TextCheckResponse textCheckResponse = wxSaveService.commentTextCheck(
                    param.getContent(),user);
            if(textCheckResponse==null){
                log.error("帖子微信安全模块检测网路异常");
            }
            else {
                if(textCheckResponse.getErrcode().equals(0)){//
                    //如果错误码正常
                    if(textCheckResponse.getResult().getSuggest().equals("risky")){//文本内容明显有问题
                        throw new BizException("您的评论有敏感词");
                    }
                    else if(textCheckResponse.getResult().getSuggest().equals("review")){//文本内容可能有问题
                        //交给管理员审核。系统不审核
                        throw new BizException("您的评论有敏感词");
                    }
                    else {//通过
                    }
                }
                else {
                    log.error("帖子微信安全模块接口错误码异常");
                }
            }
        }



        comment.setSubCommentNumber(comment.getSubCommentNumber()+1);//评论的子评论数量+1
        PostMessage postMessage = comment.getPostMessage();
        postMessage.setCommentNumber(postMessage.getCommentNumber()+1);//帖子的评论数量+1
        Message message = Message.buildWithCommentToComment(subcomment, param.getUserId());

        postMessageRepo.save(postMessage);
        subCommentRepo.save(subcomment);
        commentRepo.save(comment);
        messageRepo.save(message);

    }

    @Transactional
    public void commentToSubComment(CommentToSubCommentContentParam param){//针对评论创建一条子评论
        User user = userRepo.getReferenceById(param.getUserId());
        if(user.getDeleteFlag()){//该用户已经被封禁
            throw new BizException("您已被封禁");
        }
        if(wxTextCheck.equals("true")){
            TextCheckResponse textCheckResponse = wxSaveService.commentTextCheck(
                    param.getContent(),user);
            if(textCheckResponse==null){
                log.error("帖子微信安全模块检测网路异常");
            }
            else {
                if(textCheckResponse.getErrcode().equals(0)){//
                    //如果错误码正常
                    if(textCheckResponse.getResult().getSuggest().equals("risky")){//文本内容明显有问题
                        throw new BizException("您的评论有敏感词");
                    }
                    else if(textCheckResponse.getResult().getSuggest().equals("review")){//文本内容可能有问题
                        //交给管理员审核。系统不审核
                        throw new BizException("您的评论有敏感词");
                    }
                    else {//通过
                    }
                }
                else {
                    log.error("帖子微信安全模块接口错误码异常");
                }
            }
        }

        SubComment toSubComment = subCommentRepo.getReferenceById(param.getToSubCommentId());
        SubComment subcomment = SubComment.buildBycommentToSubComment(
                toSubComment, param.getUserId(), param.getContent());//新增一条评论记录
        Comment comment = toSubComment.getComment();
        comment.setSubCommentNumber(comment.getSubCommentNumber()+1);
        PostMessage postMessage = comment.getPostMessage();
        postMessage.setCommentNumber(postMessage.getCommentNumber()+1);//帖子的评论数量+1
        Message message = Message.buildWithCommentToSubComment(subcomment,toSubComment.getUserId());

        postMessageRepo.save(postMessage);
        subCommentRepo.save(subcomment);
        commentRepo.save(comment);
        messageRepo.save(message);
    }

    @Transactional
    public void deleteSubComment(SubCommentDeleteParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        SubComment subComment = subCommentRepo.getReferenceById(param.getSubCommentId());
        PostMessage postMessage = subComment.getComment().getPostMessage();
        Comment comment = subComment.getComment();
        if (subComment.getUserId().equals(param.getUserId())){
            subComment.setDeleteFlag(true);
            postMessage.setCommentNumber(postMessage.getCommentNumber()-1);//帖子的评论数量-1
            comment.setSubCommentNumber(comment.getSubCommentNumber()-1);//评论的评论数量-1
            commentRepo.save(comment);
            postMessageRepo.save(postMessage);
            subCommentRepo.save(subComment);
        }else {
            throw new BizException("该条评论不能由非发评人删除");
        }
    }

}
