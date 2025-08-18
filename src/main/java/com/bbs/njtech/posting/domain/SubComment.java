package com.bbs.njtech.posting.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Table(name = "sub_comment")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class SubComment {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "comment_id", length = 32)
    private String commentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Comment comment;

    @Column(name = "user_id", length = 32)
    private String userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    private String content;

    @Column(name = "comment_user_id", length = 32)
    private String commentUserId;


    private Boolean deleteFlag;

    private Date deleteTime;

    private Date publishTime;
    @Column(name = "to_sub_comment_id", length = 32)
    private String toSubCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_sub_comment_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubComment toSubComment;


    public static SubComment buildByCommentToComment(String commentId,String userId,String commentUserId,String content){
        SubComment subComment = new SubComment();
        subComment.setId(IdUtils.getId());
        subComment.setCommentId(commentId);
        subComment.setUserId(userId);
        subComment.setContent(content);
        subComment.setCommentUserId(commentUserId);
        subComment.setDeleteFlag(false);
        subComment.setPublishTime(new Date());

        return subComment;
    }
    public static SubComment buildBycommentToSubComment(SubComment toSubComment,String userId,String content){
        SubComment subComment = new SubComment();
        subComment.setId(IdUtils.getId());
        subComment.setCommentId(toSubComment.getCommentId());//新建的这个子评论肯定是要回复的那条子评论下方
        subComment.setUserId(userId);
        subComment.setContent(content);
        subComment.setCommentUserId(toSubComment.getCommentUserId());
        subComment.setToSubCommentId(toSubComment.getId());
        subComment.setDeleteFlag(false);
        subComment.setPublishTime(new Date());

        return subComment;
    }


}
