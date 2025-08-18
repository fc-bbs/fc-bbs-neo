package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.user.domain.User;
import com.bbs.njtech.user.domain.UserFavorites;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
public class UserFavoritesVO {

    private String id;//收藏的帖子的id

    private String userNickName; //收藏者的姓名

    private String userAvatarUrl;//收藏者的头像

    private String postTextContent;//收藏的帖子内容

    private String postImageUrl;//收藏的图片

    private String userMobile;//用户的联系方式

    private Integer thumbNumber;//点赞数量

    private Boolean thumbFlag; //用户是否点赞

    private Boolean favoritesFlag;//用户是否收藏

    private Integer commentNumber;//评论数量

    private Boolean commentFlag;//有无评论

    private String commentUserNickName;//评论者的昵称

    private String subCategoryName;

    private Boolean  mobileFlag;//是否提供电话



    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date commentPublishTime;//评论时间

    private String commentContent;//评论的具体内容

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date postTime;//发布时间

    private Integer favoritesNumber;//收藏数量


    public static UserFavoritesVO convertFor(UserFavorites po,HashMap<String,
            Comment> CommentMap, HashMap<String, String> thumbMap){
        if (po==null){
            return null;
        }
        UserFavoritesVO vo = new UserFavoritesVO();
        PostMessage postMessage = po.getPostMessage();
        User user = postMessage.getUser();
        vo.setId(postMessage.getId());
        vo.setUserNickName(postMessage.getUser().getNickName());
        vo.setUserAvatarUrl(user.getAvatarUrl());
        vo.setPostImageUrl(postMessage.getPostImageUrl());
        vo.setCommentNumber(postMessage.getCommentNumber());
        vo.setPostTextContent(postMessage.getPostTextContent());
        vo.setThumbNumber(postMessage.getThumbNumber());
        vo.setFavoritesNumber(postMessage.getFavoritesNumber());
        vo.setPostTime(postMessage.getPostTime());
        vo.setSubCategoryName(postMessage.getSubCategory().getName());
        vo.setFavoritesFlag(true);
        if(thumbMap.get(po.getPostMessage().getId())!=null){
            vo.setThumbFlag(thumbMap.get(po.getPostMessage().getId()).equals(Constant.点赞状态_已点赞));//如果已点赞就设置为true
        }else{
            vo.setThumbFlag(false);//如果thumbMap为空，则设置为false
        }
        if(!po.getPostMessage().getCommentNumber().equals(0)){//有评论
            vo.setCommentFlag(true);
            vo.setCommentContent(CommentMap.get(po.getPostMessage().getId()).getContent());
            vo.setCommentPublishTime(CommentMap.get(po.getPostMessage().getId()).getPublishTime());
            vo.setCommentUserNickName(CommentMap.get(po.getPostMessage().getId()).getUser().getNickName());
        }else{
            vo.setCommentFlag(false);
        }
        vo.setMobileFlag(po.getPostMessage().getUserMobile() != null);//设置是否需要提供联系方式

        if(postMessage.getState().equals(Constant.帖子状态_已删除)
                ||postMessage.getState().equals(Constant.帖子状态_被封禁)
                ||postMessage.getState().equals(Constant.帖子状态_审核不通过)){
            vo.setUserNickName("野猪君_asda9d");
            vo.setUserAvatarUrl("");
            vo.setPostImageUrl(null);
            vo.setCommentNumber(0);
            vo.setPostTextContent("该贴已经不存在");
            vo.setThumbNumber(1);
            vo.setFavoritesNumber(1);
            vo.setMobileFlag(false);
            vo.setCommentFlag(false);
        }

        return vo;

    }

    public static List<UserFavoritesVO> convertFor(List<UserFavorites> pos,HashMap<String, Comment> CommentMap,  HashMap<String, String> FavortiesHashMap){
        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<UserFavoritesVO> vos = new ArrayList<>();
        for (UserFavorites po:pos){
            vos.add(convertFor(po,CommentMap,FavortiesHashMap));
        }
        return vos;

    }






}
