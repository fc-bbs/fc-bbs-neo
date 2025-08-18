package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.posting.domain.SubComment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SubCommentVO {

    private String id;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date publishTime;

    private String content;//子评论内容

    private Boolean toSubCommentFlag;

    private String toSubCommentId;

    private String toSubCommentUserNikeName;

    private String userNickName;//评论布者昵称

    private String userAvatarUrl;//评论发布者头像


    public static List<SubCommentVO> build(List<SubComment> pos){


        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }
        List<SubCommentVO> vos= new ArrayList<>();
        for(SubComment po:pos){
            SubCommentVO vo = new SubCommentVO();
            BeanUtils.copyProperties(po,vo);

            vo.setUserNickName(po.getUser().getNickName());
            vo.setUserAvatarUrl(po.getUser().getAvatarUrl());

            if(po.getToSubComment()==null){
                vo.setToSubCommentFlag(false);
            }else {
                vo.setToSubCommentFlag(true);
                vo.setToSubCommentUserNikeName(po.getToSubComment().getUser().getNickName());

            }
            vos.add(vo);
        }
        return vos;
    }


//    public static List<SubCommentVO> build(List<SubComment> pos){
//
//
//        if (CollectionUtil.isEmpty(pos)){
//            return new ArrayList<>();
//        }
////        List<SubComment> subCommentFalgList=new ArrayList<>();//用于存储临时的
//        for(int i=0;i<pos.size();i++){
//            int offset=1;
//            for(int j=i+1;j< pos.size();j++){
//
//                if(pos.get(i).getId().equals(pos.get(j).getToSubCommentId())){//说明pos[j]是pos[i]的回复
//                    System.out.println(i+"找到了一条回复"+j);
//
//                    SubComment subComment = pos.get(j);
//                    pos.remove(j);
//                    pos.add(i+offset,subComment);
//                    offset = offset+1;
//                }
//            }//一个元素找完
//
//        }
//        List<SubCommentVO> vos= new ArrayList<>();
//        for(SubComment po:pos){
//            SubCommentVO vo = new SubCommentVO();
//            BeanUtils.copyProperties(po,vo);
//
//            vo.setUserNickName(po.getUser().getNickName());
//            vo.setUserAvatarUrl(po.getUser().getAvatarUrl());
//
//            if(po.getToSubComment()==null){
//                vo.setToSubCommentFlag(false);
//            }else {
//                vo.setToSubCommentFlag(true);
//                vo.setToSubCommentUserNikeName(po.getToSubComment().getUser().getNickName());
//
//            }
//            vos.add(vo);
//        }
//        return vos;
//    }





}
