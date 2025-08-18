package com.bbs.njtech.user.vo;


import com.bbs.njtech.user.domain.UserSubscribe;
import lombok.Data;

@Data
public class UserFansVO {
    private String id;

    private String fansName;

    private String fansAvatarUrl;

    private String fansSignature;

    private String userFansId;


    public static UserFansVO convertFor(UserSubscribe po){
        UserFansVO vo = new UserFansVO();
        vo.setId(po.getId());
        vo.setFansSignature(po.getUserSubscribe().getSignature());
        vo.setFansName(po.getUserSubscribe().getNickName());
        vo.setFansAvatarUrl(po.getUserSubscribe().getAvatarUrl());
        vo.setFansSignature(po.getUserSubscribe().getSignature());
        vo.setUserFansId(po.getUserSubscribe().getId());
        return vo;
    }

}
