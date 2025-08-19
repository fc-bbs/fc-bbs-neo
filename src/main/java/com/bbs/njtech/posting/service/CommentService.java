package com.bbs.njtech.posting.service;


import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.WxAPI.Service.WxSaveService;
import com.bbs.njtech.WxAPI.domain.TextCheckResponse;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.message.domain.Message;
import com.bbs.njtech.message.repo.MessageRepo;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.SubComment;
import com.bbs.njtech.posting.param.CommentDeleteParam;
import com.bbs.njtech.posting.param.CommentParam;
import com.bbs.njtech.posting.param.CommentPostMessageContentParam;
import com.bbs.njtech.posting.param.FindAllMyCommentParam;
import com.bbs.njtech.posting.repo.CommentRepo;
import com.bbs.njtech.posting.repo.PostMessageRepo;
import com.bbs.njtech.posting.repo.SubCommentRepo;
import com.bbs.njtech.posting.vo.CommentContentVO;
import com.bbs.njtech.posting.vo.CommentVO;
import com.bbs.njtech.posting.vo.MyCommentVO;
import com.bbs.njtech.user.domain.User;
import com.bbs.njtech.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Validated
@Service
@Slf4j
public class CommentService {
//评论服务
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private SubCommentRepo subCommentRepo;
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
    @Transactional(readOnly = true)
    public PageResult<CommentVO> findCommentByPage(CommentParam param){

        Page<Comment> result = commentRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));

        List<Comment> commentList = result.getContent();
        HashMap<String, SubComment> SubCommentHashMap = new HashMap<String, SubComment>();//用于存储第一条评论

        for(Comment comment : commentList){
            if(!comment.getSubCommentNumber().equals(0)){//有评论
                SubComment subComment=subCommentRepo.findFirstByCommentIdAndDeleteFlagOrderByPublishTimeAsc(comment.getId(),false);
                SubCommentHashMap.put(comment.getId(),subComment);
            }
        }

        return new PageResult<>(CommentVO.convertFor(commentList,SubCommentHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }
    @Transactional(readOnly = true)
    public CommentContentVO findCommentById(String commentId){
        Comment comment = commentRepo.getReferenceById(commentId);
        return CommentContentVO.convertFor(comment);
    }
    @Transactional
    public void commentPostMessage(CommentPostMessageContentParam param){
        Comment comment = Comment.buildByCommentPostMessage(param.getPostMessageId(), param.getUserId(), param.getContent());//新增一条评论记录
        User user = userRepo.getReferenceById(param.getUserId());
        if(user.getDeleteFlag()){//该用户已经被封禁
            throw new BizException("您已被封禁,无法发帖");
        }
        if(wxTextCheck.equals("true")){
            TextCheckResponse textCheckResponse = wxSaveService.commentTextCheck(param.getContent(),user);
            if(textCheckResponse==null){
                log.error("评论微信安全模块检测网路异常");
            }
            else {
                if(textCheckResponse.getErrcode().equals(0)){//
                    //如果错误码正常
                    if(textCheckResponse.getResult().getSuggest().equals("risky")){//文本内容明显有问题
                        comment.setDeleteFlag(true);
                        commentRepo.save(comment);

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



        PostMessage postMessage =postMessageRepo.getReferenceById(param.getPostMessageId());
        postMessage.setCommentNumber(postMessage.getCommentNumber()+1);//帖子评论数量+1
        Message message = Message.buildWithCommentToPostMessage(comment,postMessage.getUserId());
        commentRepo.save(comment);
        postMessageRepo.save(postMessage);
        messageRepo.save(message);//新建一个消息
    }

    @Transactional(readOnly = true)
    public PageResult<MyCommentVO> findAllMyCommentByPage(FindAllMyCommentParam param){


        Page<Comment> result = commentRepo.findAll(param.buildSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("publishTime"))));

        PageResult<MyCommentVO> pageResult = new PageResult<>(MyCommentVO.convertFor(result.getContent()),param.getPageNum(),param.getPageSize(),result.getTotalElements());
        return pageResult ;
    }




    @Transactional
    public void deleteComment(CommentDeleteParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        Comment comment = commentRepo.getReferenceById(param.getCommentId());
        PostMessage postMessage = comment.getPostMessage();

        if (comment.getUserId().equals(param.getUserId())){
            comment.setDeleteFlag(true);
            postMessage.setCommentNumber(postMessage.getCommentNumber()-comment.getSubCommentNumber()-1);

            List<SubComment> subCommentList = subCommentRepo.findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(comment.getId(),false);
            for (SubComment subComment : subCommentList){
                List<SubComment> toSubCommentList = subCommentRepo.findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(subComment.getCommentId(),false);
                for (SubComment toSubComment:toSubCommentList){
                    toSubComment.setDeleteFlag(true);
                    toSubComment.setDeleteTime(new Date());
                    subCommentRepo.save(toSubComment);

                }

                subComment.setDeleteFlag(true);
                subCommentRepo.save(subComment);
            }
            comment.setSubCommentNumber(comment.getSubCommentNumber()-subCommentList.size());

            commentRepo.save(comment);
            postMessageRepo.save(postMessage);
        }else {
            throw new BizException("该评论不能由非发评人删除");
        }
    }



}
