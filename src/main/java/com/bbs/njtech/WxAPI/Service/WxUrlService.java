package com.bbs.njtech.WxAPI.Service;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@Slf4j
@Validated
@Service
public class WxUrlService {


    @Value("${app.appid}")
    private String appid;

    @Value("${app.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;



    @Autowired
    private WxTokenService wxTokenService;
    @Transactional
    public String getGenerateUrlLink(String postMessageId){//获取加密短链URL。
        // 请求参数
        String jsonBody = "{\"path\": \"pages/home/detail\", \"query\": \"postMessageId=" + postMessageId + "\"}";
        log.info("生成短链的请求路径为："+jsonBody);

        // Access Token
        String accessToken = wxTokenService.getLocalAccessToken();

        // 请求URL
        String url = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=" + accessToken;

        // 创建OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 获取响应体
                String responseBody = response.body().string();

                // 使用 FastJSON 解析 JSON 响应体
                JSONObject jsonResponse = JSONObject.parseObject(responseBody);
                Integer errcode = jsonResponse.getIntValue("errcode");
                String errmsg = jsonResponse.getString("errmsg");
                String urlLink = jsonResponse.getString("url_link");

                if(errcode.equals(0)){//正常情况
                    return urlLink;

                }else {

                    log.error("wx获取帖子urlLink连接错误:"+errmsg);

                }
            } else {
                log.error("Request failed: " + response.code() + " - " + response.message());

            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }




}
