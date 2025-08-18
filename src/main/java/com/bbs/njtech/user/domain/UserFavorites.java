package com.bbs.njtech.user.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_favorites")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class UserFavorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "user_id",length = 32)
    private String userId;

    @Column(name = "post_message_id",length = 32)
    private String postMessageId;

    private Date createTime;

    private String state;

    @ManyToOne
    @JoinColumn(name = "user_id",updatable = false,insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_message_id",updatable = false,insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private PostMessage postMessage;


    public void doFavorite(){
        this.setState(Constant.收藏状态_收藏);

    }
    public void cancelFavorite(){
        this.setState(Constant.收藏状态_取消收藏);

    }

    public static UserFavorites build(String userId, String postMessageId){
        UserFavorites po = new UserFavorites();
        po.setId(IdUtils.getId());
        po.setUserId(userId);
        po.setPostMessageId(postMessageId);
        po.setState(Constant.收藏状态_收藏);
        po.setCreateTime(new Date());
        return po;
    }
}
