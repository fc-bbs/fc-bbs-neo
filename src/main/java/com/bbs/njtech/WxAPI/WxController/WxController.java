package com.bbs.njtech.WxAPI.WxController;

import com.alibaba.fastjson.JSONObject;
import com.bbs.njtech.posting.service.PostMessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/wx")
@Slf4j
public class WxController {



    @Autowired
    private PostMessageService postMessageService;



    @GetMapping(value = "getMessageDeliveryToken")//用于确认消息通知
    @ResponseBody
    public String get(HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) {

        if(request.getMethod().equalsIgnoreCase("get")){//用来校验，一般会验证前端配置的token等，这里简化了代码。
            log.info("接收到检测回调请求，类型为get");
            return echostr;
        }else if(request.getMethod().equalsIgnoreCase("POST")){//接收用户的相关行为事件结果
            log.info("接收到检测回调请求，类型为Post");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder requestContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestContent.append(line);
                }
                reader.close();
                //接收：{"ToUserName":"gh_ea84a199bf81","FromUserName":"oG0NJ5Oi_3Dd1HlZJ14xfnA0sJ6s","CreateTime":1686131943,"MsgType":"event","Event":"subscribe_msg_popup_event","List":{"PopupScene":"0","SubscribeStatusString":"accept","TemplateId":"4ondVRxk4L20ihrJ3iI15BDK72XatGPxE0MeCVwHasQ"}}
                log.info("接收：" + requestContent);
                return "";
            } catch (IOException e) {
                // 处理异常情况
                e.printStackTrace();
//                logger.error("异常：" + e.getMessage());
                return e.toString();
            }
        }else{
//            logger.info("不是get 或 post方法");
            return null;
        }
    }


    @PostMapping(value = "getMessageDeliveryToken")//用于确认消息通知
    @ResponseBody
    public String postMessageDeliveryToken(HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) {
        if(request.getMethod().equalsIgnoreCase("get")){//用来校验，一般会验证前端配置的token等，这里简化了代码。
            log.info("接收到检测回调请求，类型为get");
            return echostr;
        }else if(request.getMethod().equalsIgnoreCase("POST")){//接收用户的相关行为事件结果
            log.info("接收到检测回调请求，类型为Post");
            try {
                BufferedReader reader = request.getReader();
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                reader.close();
                // 使用 FastJson 解析请求体内容
                JSONObject jsonObject = JSONObject.parseObject(requestBody.toString());
                // 从 JSON 中提取 MsgType 和 Event 字段
                String msgType = jsonObject.getString("MsgType");
                String event = jsonObject.getString("Event");
                    //核心业务代码开始
                if(msgType.equals("event")){
                    if(event.equals("wxa_media_check")){
                        postMessageService.imgCallbackCheck(jsonObject);
                    }
                }
                //核心业务代码结束
                return "";
            } catch (IOException e) {
                // 处理异常情况
                e.printStackTrace();
//                logger.error("异常：" + e.getMessage());
                return e.toString();
            }
        }else{
//            logger.info("不是get 或 post方法");
            return null;
        }
    }


}
