package com.bbs.njtech.user.param;

import lombok.Data;

@Data
public class SubscribeOtherParam {
    private String userId;

    private String userSubscriberId;

    private Boolean subscribeState;
}
