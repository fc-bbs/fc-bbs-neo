package com.bbs.njtech.user.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.constants.Constant;
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
@Table(name = "user_subscribe")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class UserSubscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id",length = 32)
    private String id;

    //找我关注了谁
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_subscribed_id",updatable = false,insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    //找谁关注了我
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false,insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User userSubscribe;


    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_subscribed_id")
    private String userSubscribeId;

    @Column(name = "create_time")
    private Date createTime;

    private String state;
    public static UserSubscribe build(String userId,String userSubscribeId){
        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setId(IdUtils.getId());
        userSubscribe.setUserId(userId);
        userSubscribe.setUserSubscribeId(userSubscribeId);
        userSubscribe.setCreateTime(new Date());
        userSubscribe.setState(Constant.关注状态_已关注);
        return userSubscribe;
    }

}
