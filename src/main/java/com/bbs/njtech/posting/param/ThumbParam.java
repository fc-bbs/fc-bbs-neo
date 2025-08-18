package com.bbs.njtech.posting.param;

import lombok.Data;

@Data
public class ThumbParam {

    public Boolean thumbState; //点赞状态

    public String postMessageId; //帖子ID

    public String userId; //用户ID


}
