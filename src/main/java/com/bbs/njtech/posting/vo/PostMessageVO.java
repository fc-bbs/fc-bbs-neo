package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
@Data
public class PostMessageVO {

    private String id;

    private String userId;

    private String userNickName;//帖子发布者昵称

    private String userAvatarUrl;//帖子发布者昵称

    private String postImageUrl;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date postTime;//帖子的发布时间

    private Integer commentNumber;

    private Integer thumbNumber;

    private Boolean thumbFlag;

    private Integer favoritesNumber;

    private Boolean favoritesFlag;

    private String postTextContent;//帖子的具体内容

    private Boolean commentFlag;//有无评论

    private String commentUserNickName;//评论者的昵称

    private String subCategoryName;

    private Boolean  mobileFlag;//是否提供电话

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date commentPublishTime;//评论时间

    private String commentContent;//评论的具体内容

    private  Boolean top;


    public static List<PostMessageVO> convertFor(List<PostMessage> pos, HashMap<String, Comment> CommentMap,HashMap<String, String> thumbMap,HashMap<String, String> FavortiesHashMap){


        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<PostMessageVO> vos = new ArrayList<>();
        for (PostMessage po:pos){
            vos.add(convertFor(po,CommentMap,thumbMap,FavortiesHashMap));
        }
        return vos;
    }


    public static PostMessageVO convertFor(PostMessage po,HashMap<String, Comment> CommentMap,HashMap<String, String> thumbMap,HashMap<String, String> FavortiesHashMap){
        if (po==null){
            return null;
        }
        PostMessageVO vo = new PostMessageVO();
        BeanUtils.copyProperties(po,vo);
        vo.setUserNickName(po.getUser().getNickName());
        vo.setUserAvatarUrl(po.getUser().getAvatarUrl());

//        vo.setThumbFlag(thumbMap.get(po.getId()).equals(Constant.点赞状态_已点赞));//如果已点赞就设置为true
        //1.18新增判断点赞map是否为null
        if(thumbMap.get(po.getId())!=null){
            vo.setThumbFlag(thumbMap.get(po.getId()).equals(Constant.点赞状态_已点赞));//如果已点赞就设置为true
        }else{
            vo.setThumbFlag(false);//如果thumbMap为空，则设置为false
        }

        if(FavortiesHashMap.get(po.getId())!=null){
            vo.setFavoritesFlag(FavortiesHashMap.get(po.getId()).equals(Constant.收藏状态_收藏));//如果已收藏赞就设置为true
        }else{
            vo.setFavoritesFlag(false);//如果thumbMap为空，则设置为false
        }

        if(!po.getCommentNumber().equals(0)){//有评论
            vo.setCommentFlag(true);
            vo.setCommentContent(CommentMap.get(po.getId()).getContent());
            vo.setCommentPublishTime(CommentMap.get(po.getId()).getPublishTime());
            vo.setCommentUserNickName(CommentMap.get(po.getId()).getUser().getNickName());


        }else{
            vo.setCommentFlag(false);
        }
        vo.setMobileFlag(po.getUserMobile() != null);//设置是否需要提供联系方式

        vo.setSubCategoryName(po.getSubCategory().getName());
        return vo;
    }






}
