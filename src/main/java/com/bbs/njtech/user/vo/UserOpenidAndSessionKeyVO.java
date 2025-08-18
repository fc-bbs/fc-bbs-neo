package com.bbs.njtech.user.vo;


import lombok.Data;

@Data
public class UserOpenidAndSessionKeyVO {
    private String openid;

    private String sessionKey;

    private String appid;

    public static UserOpenidAndSessionKeyVO build(String openid, String sessionKey,String appid){
        UserOpenidAndSessionKeyVO vo = new UserOpenidAndSessionKeyVO();
        vo.setOpenid(openid);
        vo.setSessionKey(sessionKey);
        vo.setAppid(appid);
        return vo;
    }

}
