package com.bbs.njtech.card.controller;

import com.bbs.njtech.card.service.UserVipCardService;
import com.bbs.njtech.card.vo.UserVipCardVO;
import com.bbs.njtech.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userVipCard")
public class UserVipCardController {

    @Autowired
    private UserVipCardService userVipCardService;

    @GetMapping("/findUserVip")//会员信息测试
    public Result<UserVipCardVO> findUserVip(String userId){
        return Result.success(userVipCardService.findUserVip(userId));
    }
//    @GetMapping("/findUserVip")//会员信息
//    public Result<UserVipCardVO> findUserVip(){
//        return Result.success(userVipCardService.findUserVip(StpUtil.getLoginIdAsString()));
//    }

    @GetMapping("/findUserVipQr/{userId}")//二维码测试
    public ResponseEntity<ByteArrayResource> findUserVipQr(@PathVariable String userId){
        return userVipCardService.findUserVipQR(userId);
    }
//    @GetMapping("/findUserVipQr")//二维码
//    public ResponseEntity<ByteArrayResource> findUserVipQr(){
//        return userVipCardService.findUserVipQR(StpUtil.getLoginIdAsString());
//    }

    @PostMapping("/registerUserVip")//注册会员
    @ResponseBody
    public Result<String> registerUserVip(String userId){
        userVipCardService.registerUserVip(userId);
        return Result.success();
    }
}
