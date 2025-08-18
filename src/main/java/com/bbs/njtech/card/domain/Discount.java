package com.bbs.njtech.card.domain;

import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "discount")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class Discount {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "shop_id")
    private String shopId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User shop;

    @Column(name = "discount_level")
    private String discountLevel;

    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
