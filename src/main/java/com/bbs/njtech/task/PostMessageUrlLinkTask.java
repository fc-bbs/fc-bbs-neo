package com.bbs.njtech.task;


import com.bbs.njtech.posting.service.PostMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class PostMessageUrlLinkTask {

    @Autowired
    private PostMessageService postMessageService;
    @Value("${wx-generate-UrlLink}")
    private String wxGenerateUrlLink;
    @Scheduled(fixedRate = 60*1000*2)//2分钟检测一次，如果该帖子没有上链接，就生成链接
    @Transactional
    public void execute() {
        try {
            log.info("帖子补短链定时任务正常");
            if(wxGenerateUrlLink.equals("true")){
                log.info("帖子补短链定时任务打开");
                postMessageService.updateAllPostMessageWxUrlLink();
            }

        } catch (Exception e) {
            log.error("自动添加短链定时任务", e);
        }
    }

}

