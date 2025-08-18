package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MyPostMessageVO {

    private String id;

    private String userNickName;

    private String postImageUrl;

    private Date postTime;

    private String postTextContent;


    public static MyPostMessageVO convertFor(PostMessage po){
        if (po==null){
            return null;
        }

        MyPostMessageVO vo = new MyPostMessageVO();
        BeanUtils.copyProperties(po,vo);
        vo.setId(po.getId());
        vo.setUserNickName(po.getUser().getNickName());
        if(po.getState().equals(Constant.帖子状态_审核不通过)){
            vo.setPostTextContent("该帖子审核未通过："+po.getPostTextContent());
        } else if (po.getState().equals(Constant.帖子状态_被封禁)) {
            vo.setPostTextContent("该帖子已被封禁："+po.getPostTextContent());
        } else if (po.getState().equals(Constant.帖子状态_待审核)) {
            vo.setPostTextContent("该帖子审核中："+po.getPostTextContent());
        }
        return vo;
    }


    public static List<MyPostMessageVO> convertFor(List<PostMessage>pos){
        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<MyPostMessageVO>vos = new ArrayList<>();
        for (PostMessage po:pos){
            vos.add(convertFor(po));
        }
        return vos;
    }


}
