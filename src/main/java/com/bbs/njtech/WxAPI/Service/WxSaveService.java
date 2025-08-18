package com.bbs.njtech.WxAPI.Service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbs.njtech.WxAPI.domain.TextCheckResponse;
import com.bbs.njtech.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Validated
@Service
public class WxSaveService {


    @Value("${app.appid}")
    private String appid;

    @Value("${app.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WxTokenService wxTokenService;

    public TextCheckResponse postMessageTextCheck(String content, User user){//帖子内容进行检测
        String ACCESS_TOKEN = wxTokenService.getLocalAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+ACCESS_TOKEN;
        String openid = user.getOpenId();
        // 请求体内容
        String requestBodyJson = "{\"openid\": \""+openid+"\", \"scene\": 3, \"version\": 2, \"content\":\""+content+"\"}";

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);

        // 创建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 如果响应成功，则输出响应内容
                String responseBody = response.body().string();
                TextCheckResponse textCheckResponse = JSON.parseObject(responseBody, TextCheckResponse.class);
                log.info(responseBody);
                return textCheckResponse;
            } else {
                // 如果响应不成功，则输出错误信息
                System.out.println("Request failed: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // 发生异常时输出异常信息
            e.printStackTrace();
        }
        return null;
    }

    public TextCheckResponse commentTextCheck(String content, User user){//评论去检测
        String ACCESS_TOKEN = wxTokenService.getLocalAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+ACCESS_TOKEN;
        String openid = user.getOpenId();
        // 请求体内容
        String requestBodyJson = "{\"openid\": \""+openid+"\", \"scene\": 2, \"version\": 2, \"content\":\""+content+"\"}";

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);

        // 创建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 如果响应成功，则输出响应内容
                String responseBody = response.body().string();
                TextCheckResponse textCheckResponse = JSON.parseObject(responseBody, TextCheckResponse.class);
                log.info(responseBody);
                return textCheckResponse;
            } else {
                // 如果响应不成功，则输出错误信息
                System.out.println("Request failed: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // 发生异常时输出异常信息
            e.printStackTrace();
        }
        return null;

    }

    public void imgCheck(String imgUrl, User user,String postMessageId){//评论去检测
        String ACCESS_TOKEN = wxTokenService.getLocalAccessToken();
        String url = "https://api.weixin.qq.com/wxa/media_check_async?access_token="+ACCESS_TOKEN;
        String openid = user.getOpenId();
        // 请求体内容
        String requestBodyJson = "{\"openid\": \""+openid+"\", \"media_type\": 2, \"version\": 2, \"media_url\":\""+imgUrl+"\",\"scene\":3}";

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);

        // 创建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 如果响应成功，则输出响应内容
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                Integer errcode = jsonObject.getInteger("errcode");
                String trace_id = jsonObject.getString("trace_id");
                if(errcode.equals(0)){
                    redisTemplate.opsForValue().set(trace_id,postMessageId,1, TimeUnit.DAYS);//有效期一天

                }
                else {
                    log.error("图像检测模块出错："+responseBody);
                }
            } else {
                // 如果响应不成功，则输出错误信息
                System.out.println("Request failed: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // 发生异常时输出异常信息
            e.printStackTrace();
        }


    }






}
