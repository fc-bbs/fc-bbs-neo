package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.posting.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentContentVO {

    private String id;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date publishTime;

    private String content;

    private String userNickName;//评论布者昵称

    private String userAvatarUrl;//评论发布者头像

    private Integer subCommentNumber;//子评论数量


    public static List<CommentContentVO> convertFor(List<Comment> pos){


        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<CommentContentVO> vos = new ArrayList<>();
        for (Comment po:pos){
            vos.add(convertFor(po));
        }
        return vos;
    }


    public static CommentContentVO convertFor(Comment po){
        if (po==null){
            return null;
        }
        CommentContentVO vo = new CommentContentVO();
        BeanUtils.copyProperties(po,vo);

        vo.setUserNickName(po.getUser().getNickName());
        vo.setUserAvatarUrl(po.getUser().getAvatarUrl());

        return vo;
    }



}
