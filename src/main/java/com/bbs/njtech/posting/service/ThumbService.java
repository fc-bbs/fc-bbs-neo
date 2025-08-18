package com.bbs.njtech.posting.service;

import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.Thumb;
import com.bbs.njtech.posting.param.ThumbParam;
import com.bbs.njtech.posting.repo.PostMessageRepo;
import com.bbs.njtech.posting.repo.ThumbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ThumbService {

    @Autowired
     private ThumbRepo thumbRepo;

    @Autowired
    private PostMessageRepo postMessageRepo;

    @Transactional
    public Boolean updateThumbState(ThumbParam param){
        Thumb thumb =  thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(), param.getPostMessageId());//获取Thumb表的对应的一条点赞数据

        if (thumb == null){//没找到数据
           if(param.thumbState.equals(false)) {//没找到数据，前端传过来的数据也是false

               PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());//找到数据库的PostMessage实体类
               postMessage.doThumb();//帖子修改一下点赞数量
               postMessageRepo.save(postMessage);
               thumbRepo.save(Thumb.build(param.getUserId(), param.getPostMessageId()));

               return true;
           }else{
               throw new BizException("点赞信息异常");
           }

        }
        else {//找到数据
            if(param.getThumbState().equals(true)&&thumb.getState().equals(Constant.点赞状态_已点赞)){
                PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
                postMessage.cancelThumb();//取消点赞
                thumb.cancelThumb();//取消点赞
                postMessageRepo.save(postMessage);
                thumbRepo.save(thumb);
                return false;

            } else if (param.getThumbState().equals(false)&&thumb.getState().equals(Constant.点赞状态_未点赞)) {
                PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
                postMessage.doThumb();//进行点赞
                thumb.doThumb();
                thumbRepo.save(thumb);
                postMessageRepo.save(postMessage);
                return true;
            }else {
                throw new BizException("点赞信息异常");
            }
        }

    }


}
