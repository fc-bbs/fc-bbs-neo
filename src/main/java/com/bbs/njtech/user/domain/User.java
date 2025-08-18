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
@Table(name = "bbs_user")
@DynamicInsert(true)
@DynamicUpdate(true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    private String nickName;

    private Boolean sex;

    @Column(name = "college_id", length = 32)
    private  String collegeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id",updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private College college;


    private Date registerTime;

    private Date certificationTime;

    private Boolean deleteFlag;

    private String avatarUrl;

    private Date deleteTime;

    private String mobile;

    private String role;

    private String identity;

    private String signature;

    private Integer fansNumber;

    private Integer subscribeNumber;

    private String grade;//年级

    private String school;//学院

    private String openId;//openid

    public static User register(String mobile,String openId,String avatarUrl,String nickName){
        User user = new User();
        user.setId(IdUtils.getId());
        user.setNickName(nickName);
        user.setSex(Constant.用户性别_男);
        user.setCollegeId("001");
        user.setRegisterTime(new Date());
        user.setDeleteFlag(false);
        user.setMobile(mobile);
        user.setIdentity("0");//是普通用户
        user.setRole("1");//默认已经认证过了
        user.setSignature("这个人没有留下签名");
        user.setFansNumber(0);
        user.setSubscribeNumber(0);
        user.setGrade(Constant.用户年级_未知);
        user.setOpenId(openId);
        user.setAvatarUrl(avatarUrl);
        user.setSchool("未填写");


        return user;

    }

}
