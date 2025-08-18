package com.bbs.njtech.task;


import com.bbs.njtech.WxAPI.Service.WxTokenService;
import com.bbs.njtech.WxAPI.Service.WxUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class getWxAccessTokenTask {

    @Autowired
    private WxTokenService wxTokenService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WxUrlService wxUrlService;

    @Value("${wx-token-active}")
    private String wxTokenActive;


    /**
     *  微信accessToken接口得到的accessToken可以保存2小时，为了
     *  设置定时，将定时设置为 1小时55分钟 ~ 2小时之内
     *  access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     *  建议开发者使用中控服务器统一获取和刷新 access_token，其他业务逻辑服务器所使用的 access_token 均来自于该中控服务器，不应该各自去刷新，否则容易造成冲突，导致 access_token 覆盖而影响业务；
     *  access_token 的有效期通过返回的 expires_in 来传达，目前是7200秒之内的值，中控服务器需要根据这个有效时间提前去刷新。在刷新过程中，中控服务器可对外继续输出的老 access_token，此时公众平台后台会保证在5分钟内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
     *  access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，这样便于业务服务器在API调用获知 access_token 已超时的情况下，可以触发 access_token 的刷新流程。
     */
    @Scheduled(fixedDelay = 7080000)
    public void execute() {
        try {
            if(wxTokenActive.equals("true")){
                String accessToken = wxTokenService.getNewAccessToken();
                if(!accessToken.equals("error")){
                    redisTemplate.opsForValue().set("ACCESS_TOKEN_KEY", accessToken, 7200, TimeUnit.SECONDS);

//                    wxUrlService.getGenerateUrlLink("1762835489825488896");

                }else{
                    log.error("获取token连接出错");
                }
            }
            else {
                log.info("未开启token获取接口");
            }

//            System.out.println("执行一次计算任务");
        } catch (Exception e) {
            log.error("获取token", e);
        }
    }

}

