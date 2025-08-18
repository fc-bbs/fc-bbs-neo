package com.bbs.njtech.posting.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.posting.param.ThumbParam;
import com.bbs.njtech.posting.service.ThumbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thumb")
public class ThumbController {


    @Autowired
    private ThumbService thumbService;

    @PostMapping("/updateThumbState")
    public Result<Boolean> updateThumbState(ThumbParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        return Result.success(thumbService.updateThumbState(param));
    }


}
