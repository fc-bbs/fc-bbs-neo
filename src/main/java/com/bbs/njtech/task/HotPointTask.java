package com.bbs.njtech.task;


import com.bbs.njtech.posting.service.PostMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotPointTask {


    @Autowired
    private PostMessageService postMessageService;

    @Scheduled(fixedRate = 15*60*1000)//15分钟检测一次
    public void execute() {
        try {

            postMessageService.calculateHotPoint();
//            System.out.println("执行一次计算任务");
        } catch (Exception e) {
            log.error("计算热门超时定时任务", e);
        }
    }

}

