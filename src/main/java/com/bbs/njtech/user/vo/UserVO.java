package com.bbs.njtech.user.vo;

import com.bbs.njtech.user.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserVO {
    private String nickName;

    private String role;

    private String signature;

    private String userSchool;

    private String avatarUrl;

    private Integer subscribeNumber;//

    private Integer fansNumber;

    private Boolean subscribeFlag;//当前登录用户是否关注了正在查看的用户
    public static UserVO convertFor(User po){
        if (po==null){
            return null;
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(po,vo);
        vo.setUserSchool(po.getCollege().getName());
        return vo;

    }

    public static UserVO buildUser(User po,Boolean subscribeFlag){
        if (po==null){
            return null;
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(po,vo);
        vo.setSubscribeFlag(subscribeFlag);
        return vo;
    }


    public static UserVO convertForWithUnLogin(String collegeName,String avatarUrl){

        UserVO vo = new UserVO();
        vo.setNickName("您还未登录");
        vo.setUserSchool("");
        vo.setSignature("暂无签名");

        vo.setRole("1");
        vo.setSubscribeFlag(false);
        vo.setAvatarUrl(avatarUrl);
        vo.setFansNumber(0);
        vo.setSubscribeNumber(0);
        return vo;

    }

}
