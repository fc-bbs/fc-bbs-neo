package com.bbs.njtech.card.domain;

import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@Table(name = "trade")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class Transaction {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "card_id", nullable = false)
    private String cardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private UserVipCard card;

    @Column(name = "coupons_id", nullable = false)
    private String couponsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupons_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Coupons coupon;

    @Column(name = "customer_id", nullable = false)
    private String customerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User customer;

    @Column(name = "shop_id", nullable = false)
    private String shopId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User shop;

    @Column(name = "trade_type", nullable = false)
    private String tradeType;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_type", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Discount discount;

    @Column(name = "price", nullable = false)
    private BigDecimal amount;

    @Column(name = "money", nullable = false)
    private BigDecimal money;

    @Column(name = "trade_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionTime;
}
