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
public class MyCommentVO {
    private String id;

    private String userNickName;

//    private String postImageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date commentTime;

    private String commentTextContent;


    public static MyCommentVO convertFor(Comment po){
        if (po==null){
            return null;
        }

        MyCommentVO vo = new MyCommentVO();
        BeanUtils.copyProperties(po,vo);
        vo.setCommentTime(po.getPublishTime());
        vo.setUserNickName(po.getUser().getNickName());
        vo.setCommentTextContent(po.getContent());
        return vo;
    }


    public static List<MyCommentVO> convertFor(List<Comment>pos){
        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<MyCommentVO>vos = new ArrayList<>();
        for (Comment po:pos){
            vos.add(convertFor(po));
        }
        return vos;
    }
}
