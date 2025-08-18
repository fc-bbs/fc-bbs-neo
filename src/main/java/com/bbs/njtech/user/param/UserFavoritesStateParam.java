package com.bbs.njtech.user.param;


import lombok.Data;


@Data
public class UserFavoritesStateParam {

    private String userId;

    public Boolean favoritesState; //点赞状态

    public String postMessageId; //帖子ID

}
