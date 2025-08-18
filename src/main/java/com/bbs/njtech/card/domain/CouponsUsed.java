package com.bbs.njtech.card.domain;

import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "coupons_use")
public class CouponsUsed {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "coupons_id")
    private String couponsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupons_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Coupons coupons;

    @Column(name = "user_id")
    private String userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "state")
    private String state;

    @Column(name = "receive_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;

    @Column(name = "use_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useTime;

    @Column(name = "QRcode")
    private String qrCode;
}
