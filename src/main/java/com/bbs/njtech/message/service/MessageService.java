package com.bbs.njtech.message.service;

import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.message.domain.Message;
import com.bbs.njtech.message.param.MessageDeleteParam;
import com.bbs.njtech.message.param.MessageDetectEffectiveParam;
import com.bbs.njtech.message.param.MessageParam;
import com.bbs.njtech.message.repo.MessageRepo;
import com.bbs.njtech.message.vo.MessageVO;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.SubComment;
import com.bbs.njtech.posting.repo.CommentRepo;
import com.bbs.njtech.posting.repo.PostMessageRepo;
import com.bbs.njtech.posting.repo.SubCommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private PostMessageRepo postMessageRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private SubCommentRepo subCommentRepo;
    @Transactional
    public PageResult<MessageVO>findLatestMessageByPage(MessageParam param){

        Page<Message> result = messageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
        readMessage(result.getContent());//将获取到的信息设置为已读
        return new PageResult<>(MessageVO.convertFor( result.getContent()),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );
    }
    @Transactional
    public PageResult<MessageVO>findLatestMessageByPageWithUnLogin(MessageParam param){

        Page<Message> result = messageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
        readMessage(result.getContent());//将获取到的信息设置为已读
        return new PageResult<>(MessageVO.convertFor( result.getContent()),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );
    }

    @Transactional
    public void deleteMessage(MessageDeleteParam param){
        Message message = messageRepo.getReferenceById(param.getMessageId());
        if(message.getUserId().equals(param.getUserId())){
            message.setState(Constant.消息状态_已删除);
            messageRepo.save(message);
        }else {
            throw new BizException("只能由消息本人删除消息");
        }

    }
    @Transactional(readOnly = true)
    public Boolean detectMessageEffective(MessageDetectEffectiveParam param){//检测消息是否能进入到对应界面里
        Message message = messageRepo.getReferenceById(param.getMessageId());

        if(message.getUserId().equals(param.getUserId())){
            //查询这个消息对应的内容是否还在，
                if(message.getType().equals(Constant.消息类型_他人回复帖子)){
                    PostMessage postMessage = message.getPostMessage();
                    if(!postMessage.getState().equals(Constant.帖子状态_已发布)){//如果这条帖子不是已发布状态的话
                        return false;
                    }
                }
                if(message.getType().equals(Constant.消息类型_他人回复评论)){
                    Comment comment = message.getComment();
                    if(comment.getDeleteFlag().equals(true)){//已经被删除
                        return false;

                    }
                }
                if(message.getType().equals(Constant.消息类型_他人回复子评论)){//查看这是不是一条回复子评论
                    SubComment subcomment = message.getSubComment();
                    if(subcomment.getDeleteFlag().equals(true)){//已经被删除
                        return false;
                    }
                }
        }else {
            throw new BizException("非消息本人，不可查询消息状态");
        }
        return true;
    }


//    @Transactional
//    public void readMessage(MessageReadParam param){//输入一个检测阅读某条消息
//        Message message = messageRepo.getReferenceById(param.getMessageId());
//        if(message.getState().equals(Constant.消息状态_已删除)){
//            throw new BizException("该消息已删除");
//        }
//        if(message.getState().equals(Constant.消息状态_已读)){
//            return;
//        }
//        if(message.getState().equals(Constant.消息状态_未读)){
//            message.setState(Constant.消息状态_已读);
//            messageRepo.save(message);
//        }
//    }

    @Transactional//阅读消息
    public void readMessage(List<Message> messages){//阅读消息
        for(Message message:messages) {
            if (message.getState().equals(Constant.消息状态_未读)) {
                message.setState(Constant.消息状态_已读);
                messageRepo.save(message);
            }
        }
    }
    @Transactional//阅读消息
    public Integer getUnreadMessageNumber(String userId){//阅读消息
        return messageRepo.countByUserIdAndState(userId,Constant.消息状态_未读);

    }

}
