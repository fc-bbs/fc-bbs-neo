package com.bbs.njtech.posting.vo;

import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.Thumb;
import com.bbs.njtech.user.domain.UserFavorites;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class PostMessageDetailVO {

    private String id;

    private String userId;

    private String userNickName;//帖子发布者昵称

    private String userAvatarUrl;//帖子发布者头像

    private String postImageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date postTime;//帖子的发布时间

    private Integer commentNumber;

    private Integer thumbNumber;

    private Boolean thumbFlag;

    private String postTextContent;//帖子的具体内容

    private String subCategoryName;

    private Boolean favoritesFlag;

    private Integer favoritesNumber;

    private Boolean  mobileFlag;//是否提供电话


    public static PostMessageDetailVO convertFor(PostMessage po, Thumb thumb, UserFavorites favorites){
        if (po==null){
            return null;
        }
        PostMessageDetailVO vo = new PostMessageDetailVO();
        BeanUtils.copyProperties(po,vo);
        vo.setUserNickName(po.getUser().getNickName());
        vo.setUserAvatarUrl(po.getUser().getAvatarUrl());
        vo.setSubCategoryName(po.getSubCategory().getName());
        if(thumb==null){
            vo.setThumbFlag(false);//没找到记录
        }else {
            vo.setThumbFlag(thumb.getState().equals(Constant.点赞状态_已点赞));//找到记录。跟记录状态一致
        }
        if(favorites==null){
            vo.setFavoritesFlag(false);//没找到记录
        }else {
            vo.setFavoritesFlag(favorites.getState().equals(Constant.收藏状态_收藏));//找到记录。跟记录状态一致
        }
        vo.setMobileFlag(po.getUserMobile() != null);//设置是否需要提供联系方式
        return vo;
    }






}
