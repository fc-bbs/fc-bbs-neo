package com.bbs.njtech.posting.param;

import cn.hutool.core.util.StrUtil;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import lombok.Data;

import java.util.Date;

@Data
public class PostMessageContentParam {

    private String postTextContent;//接受帖子的具体内容

    private String postImageUrl;//接收帖子的图片信息

    private String subCategoryName;//子类的name

    private String userId;//发帖人的Id号

    private String userMobile;//发帖人手机号

    private Date expirationDate;//有效期年数
    public PostMessage convertToPo() {

        PostMessage postMessage = new PostMessage();
        postMessage.setId(IdUtils.getId());
        //1.19修改
        if ((this.getPostTextContent() == null || "undefined".equals(this.getPostTextContent()) || StrUtil.isBlank(this.getPostTextContent())
        )
                &&
                (this.getPostImageUrl() == null || "undefined".equals(this.getPostImageUrl()) || StrUtil.isBlank(this.getPostImageUrl())
                )
        ) {
            System.out.println(this.getPostTextContent());
            throw new BizException("帖子内容不得为空");
        }

        postMessage.setPostTextContent(this.getPostTextContent());
        //后续可能要加上拼接图片的Url
        postMessage.setPostImageUrl(this.getPostImageUrl());

        //2.6

        if(!(this.getUserMobile().equals("")||this.getUserMobile()==null)){//不是空值也不是null，说明有值
            postMessage.setUserMobile(this.getUserMobile());
            postMessage.setMobileDeadlineTime(this.getExpirationDate());

        }


        //发帖时间为当前的系统时间
        postMessage.setPostTime(new Date());
        postMessage.setState(Constant.帖子状态_待审核);

        //向数据库传入当前发贴用户的Id
        postMessage.setUserId(this.getUserId());
        postMessage.setThumbNumber(0);
        postMessage.setCommentNumber(0);
        postMessage.setFavoritesNumber(0);
        postMessage.setHotPoint(0);
        postMessage.setTop(false);

        postMessage.setType(Constant.帖子类型_普通帖子);
        return postMessage;
    }

}