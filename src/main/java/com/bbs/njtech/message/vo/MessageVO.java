package com.bbs.njtech.message.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.message.domain.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MessageVO {

    private String id;

    private String userId;//发送者的id

    private String content;

    private String postMessageId;

//    private String state;

    private String type;

    private String userNickName;//这个是发送者的昵称

    private String userAvatarUrl;//这个是发送者的头像

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    private String commentId;




    public static List<MessageVO> convertFor(List<Message> pos){


        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<MessageVO> vos = new ArrayList<>();
        for (Message po:pos){
            vos.add(convertFor(po));
        }
        return vos;
    }


    public static MessageVO convertFor(Message po){
        if (po==null){
            return null;
        }
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(po,vo);
        vo.setUserId(po.getSenderUserId());
        vo.setUserNickName(po.getSenderUser().getNickName());
        vo.setUserAvatarUrl(po.getSenderUser().getAvatarUrl());
        if(po.getType().equals(Constant.消息类型_他人回复子评论)){
            //如果这条消息是某人发出的三级评论，则把三级评论对应的commentId找到赋值。
            vo.setCommentId(po.getSubComment().getCommentId());
        }
//        if(po.getState().equals(Constant.消息状态_未读)){
//            vo.setState("unRead");
//        }
//        if(po.getState().equals(Constant.消息状态_已读)){
//            vo.setState("hasRead");
//        }

        return vo;
    }






}
