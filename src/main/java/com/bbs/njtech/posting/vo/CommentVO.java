package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.SubComment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
public class CommentVO {

    private String id;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date publishTime;

    private String content;

    private String userNickName;//评论布者昵称

    private String userAvatarUrl;//评论发布者头像

    private Boolean subCommentFlag;//有无子评论

    private String subCommentUserNickName;//子评论发布者的昵称

    private String subCommentContent;//子评论发布者的内容

    private Integer subCommentNumber;

    public static List<CommentVO> convertFor(List<Comment> pos, HashMap<String, SubComment> SubCommentMap){


        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<CommentVO> vos = new ArrayList<>();
        for (Comment po:pos){
            vos.add(convertFor(po,SubCommentMap));
        }
        return vos;
    }


    public static CommentVO convertFor(Comment po,HashMap<String, SubComment> SubCommentMap){
        if (po==null){
            return null;
        }
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(po,vo);

        vo.setUserNickName(po.getUser().getNickName());
        vo.setUserAvatarUrl(po.getUser().getAvatarUrl());
        vo.setSubCommentNumber(po.getSubCommentNumber());
        if(SubCommentMap.get(po.getId())!=null){//有东西
            vo.setSubCommentContent(SubCommentMap.get(po.getId()).getContent());
            vo.setSubCommentFlag(true);
            vo.setSubCommentUserNickName(SubCommentMap.get(po.getId()).getUser().getNickName());

        }else{
            vo.setSubCommentFlag(false);
        }

        return vo;
    }



}
