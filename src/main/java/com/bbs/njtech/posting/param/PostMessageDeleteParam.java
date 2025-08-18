package com.bbs.njtech.posting.param;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class PostMessageDeleteParam {

    private String postMessageId;

    private String userId;



}
