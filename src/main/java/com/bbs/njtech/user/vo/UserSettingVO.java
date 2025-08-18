package com.bbs.njtech.user.vo;

import com.bbs.njtech.user.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
@Data
public class UserSettingVO {
    private String nickName;

    private String role;

    private String signature;

    private String grade;//年级

    private String school;//学院

    private String userCollage;//用户所在学校

    private String avatarUrl;

    private Boolean sex;

    private String mobile;




    public static UserSettingVO convertFor(User po){
        if (po==null){
            return null;
        }

        UserSettingVO vo = new UserSettingVO();
        BeanUtils.copyProperties(po,vo);
        vo.setUserCollage(po.getCollege().getName());
        return vo;

    }
}
