package com.bbs.njtech.posting.domain;

import com.bbs.njtech.user.domain.College;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Table(name = "bbs_general_category")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class GeneralCategory {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "college_id", length = 32)
    private String collegeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private College college;

    private String name;

    private Boolean deleteFlag;

    private Date deleteTime;

    private Date createTime;

    private Boolean isActivity;//查看是否是活动

    private Boolean isService;//是否是服务
}
