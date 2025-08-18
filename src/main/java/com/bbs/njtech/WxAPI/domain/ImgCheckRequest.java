package com.bbs.njtech.WxAPI.domain;

import lombok.Data;

import java.util.List;
@Data
public class ImgCheckRequest {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String Event;
    private String appid;
    private String trace_id;
    private Integer version;
    private List<Detail> detail;
    private Integer errcode;
    private String errmsg;
    private Result result;

    @Data
    class Detail {
        private String strategy;
        private Integer errcode;
        private String suggest;
        private Integer label;
        private Integer prob;

        // 添加相应的 getters 和 setters 方法
        // 省略其他方法
    }
    @Data
    class Result {
        private String suggest;
        private Integer label;

        // 添加相应的 getters 和 setters 方法
        // 省略其他方法
    }
}



