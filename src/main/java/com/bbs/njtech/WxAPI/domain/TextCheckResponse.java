package com.bbs.njtech.WxAPI.domain;

import lombok.Data;

import java.util.List;
@Data
public class TextCheckResponse {
    private Integer errcode;
    private String errmsg;
    private List<Detail> detail;
    private String trace_id;
    private Result result;
    @Data
    public static class Detail {
        private String strategy;
        private Integer errcode;
        private String suggest;
        private Integer label;
        private Integer prob;
    }
    @Data
    public static class Result {
        private String suggest;
        private Integer label;
    }
}

