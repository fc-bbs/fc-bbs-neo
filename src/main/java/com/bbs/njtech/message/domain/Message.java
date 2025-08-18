package com.bbs.njtech.message.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.SubComment;
import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Table(name = "message")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class Message {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "user_id")
    private String userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    private String state;

    private String content;

    private String type;

    @Column(name = "post_message_id")
    private String postMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_message_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private PostMessage postMessage;

    private Date createTime;

    @Column(name = "comment_id", length = 32)
    private String commentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Comment comment;

    @Column(name = "sub_comment_id", length = 32)
    private String subCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_comment_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubComment subComment;

    @Column(name = "sender_user_id")
    private String senderUserId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User senderUser;

    public static Message buildWithCommentToPostMessage(Comment comment,String postMessageUserId){
        Message message = new Message();
        message.setId(IdUtils.getId());
//        message.setUserId(comment.getPostMessage().getUserId());//应该是发帖人收到消息
        message.setUserId(postMessageUserId);//应该是发帖人收到消息
        message.setSenderUserId(comment.getUserId());
        if(comment.getContent().length()>15){
            message.setContent("评论了您:"+comment.getContent().substring(0,14)+"...");
        }else{
            message.setContent("评论了您:"+comment.getContent());
        }
        message.setType(Constant.消息类型_他人回复帖子);
        message.setState(Constant.消息状态_未读);
        message.setPostMessageId(comment.getPostMessageId());
        message.setCreateTime(new Date());
        return message;

    }

    public static Message buildWithCommentToComment(SubComment subcomment,String userId){
        Message message = new Message();
        message.setId(IdUtils.getId());
        message.setUserId(subcomment.getCommentUserId());//被回复的人的userId
        message.setSenderUserId(subcomment.getUserId());//发送者的userId
        if(subcomment.getContent().length()>15){
            message.setContent("评论了您:"+subcomment.getContent().substring(0,14)+"...");
        }else{
            message.setContent("评论了您:"+subcomment.getContent());
        }
        message.setType(Constant.消息类型_他人回复评论);
        message.setState(Constant.消息状态_未读);
        message.setCommentId(subcomment.getCommentId());
        message.setCreateTime(new Date());
        return message;

    }

    public static Message buildWithCommentToSubComment(SubComment subcomment,String toSubCommentUserId){//当前这个subcomment是三级评论
        Message message = new Message();
        message.setId(IdUtils.getId());
        message.setUserId(toSubCommentUserId);//被回复的人是二级评论的作者
        message.setSenderUserId(subcomment.getUserId());//发送方是三级评论的作者
        if(subcomment.getContent().length()>15){
            message.setContent("评论了您:"+subcomment.getContent().substring(0,14)+"...");
        }else{
            message.setContent("评论了您:"+subcomment.getContent());
        }
        message.setType(Constant.消息类型_他人回复子评论);
        message.setState(Constant.消息状态_未读);
        message.setSubCommentId(subcomment.getId());
        message.setCreateTime(new Date());
        return message;

    }

    public static Message buildWithPostMessageNotPass(PostMessage postMessage){// 审核帖子不通过
        Message message = new Message();
        message.setId(IdUtils.getId());
        message.setUserId(postMessage.getUserId());//
        message.setSenderUserId(postMessage.getUserId());//
        if(postMessage.getPostTextContent().length()>11){
            message.setContent("您的帖子:\""+postMessage.getPostTextContent().substring(0,10)+"\"审核不通过");
        }else{
            message.setContent("您的帖子:\""+postMessage.getPostTextContent()+"\"审核不通过");
        }

        message.setType(Constant.消息类型_帖子审核不通过);
        message.setState(Constant.消息状态_未读);
        message.setPostMessageId(postMessage.getId());
        message.setCreateTime(new Date());
        return message;

    }
}
