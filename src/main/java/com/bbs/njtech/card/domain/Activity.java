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
@Table(name = "activity")
public class Activity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "activity_title", nullable = false, length = 255)
    private String activityTitle;

    @Column(name = "begin_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "detail", nullable = false, length = 255)
    private String detail;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;

    @Column(name = "delete_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteTime;

    @Column(name = "activity_url")
    private String activityUrl;

}
