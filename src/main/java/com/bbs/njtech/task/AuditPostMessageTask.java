package com.bbs.njtech.task;


import com.bbs.njtech.posting.service.PostMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class AuditPostMessageTask {

    @Autowired
    private PostMessageService postMessageService;

    @Scheduled(fixedRate = 60*1000)//1分钟检测一次
    @Transactional
    public void execute() {
        try {
            postMessageService.auditPostMessage();
//            System.out.println("执行一次计算任务");
        } catch (Exception e) {
            log.error("自动审核帖子定时任务", e);
        }
    }

}

