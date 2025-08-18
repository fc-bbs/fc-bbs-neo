package com.bbs.njtech.user.param;

import lombok.Data;

@Data
public class UpdateUserSettingParam {
    private String id;
    private String avatarUrl;
    private String nickName;
    private String signature;
    private String grade;//年级
    private String school;//学院
    private Boolean sex;

}
