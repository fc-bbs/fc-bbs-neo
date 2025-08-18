package com.bbs.njtech.message.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.message.param.MessageDeleteParam;
import com.bbs.njtech.message.param.MessageDetectEffectiveParam;
import com.bbs.njtech.message.param.MessageParam;
import com.bbs.njtech.message.service.MessageService;
import com.bbs.njtech.message.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @GetMapping("/findLatestMessageByPage")//查询当前消息
    public Result<PageResult<MessageVO>> findLatestMessageByPage(MessageParam param){
        if(StpUtil.isLogin()) {
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(messageService.findLatestMessageByPage(param));
        }else {
            param.setUserId("xxxx");
            return Result.success(messageService.findLatestMessageByPageWithUnLogin(param));
        }



    }
    @PostMapping("/deleteMessage")//删除当前消息
    public Result<String> deleteMessage(MessageDeleteParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        messageService.deleteMessage(param);
        return Result.success();
    }
    @GetMapping("/detectMessageEffective")//检测当前这个消息是否有效
    public Result<Boolean> detectMessageEffective(MessageDetectEffectiveParam param) {
        param.setUserId(StpUtil.getLoginIdAsString());
        return Result.success( messageService.detectMessageEffective(param));
    }
    @GetMapping("/getUnreadMessageNumber")//检测当前这个消息是否有效
    public Result<Integer> getUnreadMessageNumber() {

        if(StpUtil.isLogin()){
            String userId = StpUtil.getLoginIdAsString();
            return Result.success( messageService.getUnreadMessageNumber(userId));
        }else {
            return Result.success(0);
        }


    }

//    @PostMapping("/readMessage")//查询当前消息
//    public Result<String> readMessage(MessageReadParam param){
//        param.setUserId(StpUtil.getLoginIdAsString());
//        messageService.readMessage(param);
//        return Result.success();
//    }

}
