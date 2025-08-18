package com.bbs.njtech.WxAPI.Service;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
@Validated
@Service
public class WxTokenService {

    @Value("${app.appid}")
    private String appid;

    @Value("${app.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;
    public String getNewAccessToken(){//输入前端的内容
        String grantType = "client_credential";

        // 请求URL
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grantType + "&appid=" + appid + "&secret=" + secret;

        // 创建OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 获取响应体
                String responseBody = response.body().string();

                // 使用Fastjson解析JSON响应
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                String accessToken = jsonObject.getString("access_token");

                System.out.println("Access Token: " + accessToken);
                return accessToken;
            } else {
                System.out.println("Request failed: " + response.code() + " - " + response.message());
                return "error";
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "error";
    }

    public String getLocalAccessToken(){//获取本地的token
        return redisTemplate.opsForValue().get("ACCESS_TOKEN_KEY");
    }



}
