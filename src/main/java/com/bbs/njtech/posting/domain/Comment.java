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
@Table(name = "comment")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class Comment {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "post_message_id")
    private String postMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_message_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private PostMessage postMessage;

    private Date publishTime;

    @Column(name = "user_id")
    private String userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    private Boolean deleteFlag;

    private Date deleteTime;

    private String content;

    @Column(name = "sub_comment_number")
    private Integer subCommentNumber;

    private Integer hotPoint;


    public static Comment buildByCommentPostMessage(String postMessageId,String userId,String content){
        Comment comment = new Comment();
        comment.setId(IdUtils.getId());
        comment.setPostMessageId(postMessageId);
        comment.setPublishTime(new Date());
        comment.setUserId(userId);
        comment.setDeleteFlag(false);
        comment.setContent(content);
        comment.setSubCommentNumber(0);
        comment.setHotPoint(0);
        return comment;
    }

}
