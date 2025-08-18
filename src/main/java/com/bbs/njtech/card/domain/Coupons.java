package com.bbs.njtech.card.domain;

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
@Table(name = "coupons")
public class Coupons {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "activity_id")
    private String activityId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Activity activity;

    @Column(name = "coupons_name")
    private String couponsName;

    @Column(name = "expiration_time")//到期时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationTime;

    @Column(name = "detail")
    private String detail;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "range", nullable = false, length = 32)
    private String range;


}