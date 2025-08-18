package com.bbs.njtech.card.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

import static com.bbs.njtech.card.service.UserVipCardService.randomCode;

@Getter
@Setter
@Table(name = "user_vip_card")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class UserVipCard {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "user_id")
    private String userId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "QRcode")
    private String qrCode;//二维码图片

    @Column(name = "level")
    private String level;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "member_point")
    private String memberPoint;

    @Column(name = "detail")
    private String detail;

    @Column(name = "member_url")
    private String memberUrl;

    public static UserVipCard build(String userId){
        UserVipCard card = new UserVipCard();
        card.setId(IdUtils.getId());
        card.setUserId(userId);
        card.setQrCode(randomCode());
        card.setLevel("1");
        card.setMemberPoint("0");
        card.setExpirationTime(new Date());
        return card;
    }



}
