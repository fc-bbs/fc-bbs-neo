package com.bbs.njtech.user.vo;


import com.bbs.njtech.user.domain.UserSubscribe;
import lombok.Data;

@Data
public class UserSubscribeVO {

    private String id;

    private String subscribeName;

    private String subscribeAvatarUrl;

    private String subscribeSignature;

    private String userSubscribeId;

    private Boolean subscribeFlag;

    public static UserSubscribeVO convertFor(UserSubscribe po,Boolean subscribeFlag){
        UserSubscribeVO vo = new UserSubscribeVO();
        vo.setId(po.getId());
        vo.setSubscribeAvatarUrl(po.getUser().getAvatarUrl());
        vo.setSubscribeName(po.getUser().getNickName());
        vo.setSubscribeSignature(po.getUser().getSignature());
        vo.setUserSubscribeId(po.getUser().getId());
        vo.setSubscribeFlag(subscribeFlag);
        return vo;
    }
}
