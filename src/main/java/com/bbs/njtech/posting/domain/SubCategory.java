package com.bbs.njtech.posting.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Table(name = "sub_category")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class SubCategory {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "general_category_id", length = 32)
    private String generalCategoryId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_category_id",updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private GeneralCategory generalCategory;

    private String name;

    private Boolean deleteFlag;

    private Date deleteTime;

    private Date createTime;
}
