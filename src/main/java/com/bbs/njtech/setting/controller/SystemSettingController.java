package com.bbs.njtech.setting.controller;

import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.posting.param.PostMessageParam;
import com.bbs.njtech.posting.service.PostMessageService;
import com.bbs.njtech.posting.vo.PostMessageVO;
import com.bbs.njtech.setting.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemSetting")
public class SystemSettingController {


    @Autowired
    private SystemSettingService systemSettingService;

    @Autowired
    private PostMessageService postMessageService;

    @GetMapping("/findAppContactUsImgUrl")
    public Result<String> findAppContactUsImgUrl(){
        return Result.success(systemSettingService.findAppContactUsImgUrl());
    }

    @GetMapping("/findAppAvatarUrl")
    public Result<String> findAppAvatarUrl(){
        return Result.success(systemSettingService.findAppAvatarUrl());
    }

    @GetMapping("/findAppGroupImg")
    public Result<String> findAppGroupImg(){
        return Result.success(systemSettingService.findAppGroupImg());
    }

    @GetMapping("/findAppName")
    public Result<String> findAppName(){
        return Result.success(systemSettingService.findAppName());
    }

    @GetMapping("/findAppOfficialAccountImg")
    public Result<String> findAppOfficialAccountImg(){
        return Result.success(systemSettingService.findAppOfficialAccountImg());
    }


    @PostMapping("/findLatestPostMessageByPage")
    @ResponseBody
    public Result<PageResult<PostMessageVO>> findLatestPostMessageByPage(PostMessageParam param){
        return Result.success(postMessageService.findLatestPostMessageByPage(param));
    }
}
