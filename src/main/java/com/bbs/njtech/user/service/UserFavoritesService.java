package com.bbs.njtech.user.service;


import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.Comment;
import com.bbs.njtech.posting.domain.PostMessage;
import com.bbs.njtech.posting.domain.Thumb;
import com.bbs.njtech.posting.param.UserFavoritesParam;
import com.bbs.njtech.posting.repo.CommentRepo;
import com.bbs.njtech.posting.repo.PostMessageRepo;
import com.bbs.njtech.posting.repo.ThumbRepo;
import com.bbs.njtech.posting.vo.UserFavoritesVO;
import com.bbs.njtech.user.domain.UserFavorites;
import com.bbs.njtech.user.param.UserFavoritesStateParam;
import com.bbs.njtech.user.repo.UserFavoritesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;

@Validated
@Service
public class UserFavoritesService {

    @Autowired
    private UserFavoritesRepo userFavoritesRepo;

    @Autowired
    private PostMessageRepo postMessageRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ThumbRepo thumbRepo;

    @Transactional
    public Boolean updateFavoritesState(UserFavoritesStateParam param) {
        UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(), param.getPostMessageId());//获取表的对应的一条点赞数据


        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
        if(postMessage.getUserId().equals(param.getUserId())){
            throw new BizException("自己不能收藏自己的帖子");
        }
        if (userFavorites == null) {//没找到数据
            if (param.getFavoritesState().equals(false)) {//没找到数据，前端传过来的数据也是false


                postMessage.doFavorite();//帖子修改一下点赞数量
                postMessageRepo.save(postMessage);
                userFavoritesRepo.save(UserFavorites.build(param.getUserId(), param.getPostMessageId()));

                return true;
            } else {
                throw new BizException("收藏信息异常");
            }

        } else {//找到数据
            if (param.getFavoritesState().equals(true) && userFavorites.getState().equals(Constant.收藏状态_收藏)) {

                postMessage.cancelFavorite();//取消收藏
                userFavorites.cancelFavorite();//取消收藏
                postMessageRepo.save(postMessage);
                userFavoritesRepo.save(userFavorites);
                return false;

            } else if (param.getFavoritesState().equals(false) && userFavorites.getState().equals(Constant.收藏状态_取消收藏)) {

                postMessage.doFavorite();//进行点赞
                userFavorites.doFavorite();
                userFavoritesRepo.save(userFavorites);
                postMessageRepo.save(postMessage);
                return true;
            } else {
                throw new BizException("收藏信息异常");
            }
        }


    }

    @Transactional(readOnly = true)
    public PageResult<UserFavoritesVO> findAllUserFavorites(UserFavoritesParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        Page<UserFavorites> result = userFavoritesRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));

        List<UserFavorites> userFavoritesList = result.getContent();

        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息


        for (UserFavorites userFavorites : userFavoritesList){


            if(!userFavorites.getPostMessage().getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(userFavorites.getPostMessage().getId(),false);
                CommentHashMap.put(userFavorites.getPostMessage().getId(),comment);
            }
            if(!userFavorites.getPostMessage().getThumbNumber().equals(0)){//有点赞
                Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(StpUtil.getLoginIdAsString(),userFavorites.getPostMessage().getId());
                if(thumb!=null){//有记录
                    if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                        ThumbHashMap.put(userFavorites.getPostMessage().getId(),Constant.点赞状态_已点赞);
                    }
                    else ThumbHashMap.put(userFavorites.getPostMessage().getId(),Constant.点赞状态_未点赞);
                }else{
                    ThumbHashMap.put(userFavorites.getPostMessage().getId(),Constant.点赞状态_未点赞);
                }

            }


        }

        PageResult<UserFavoritesVO> pageResult = new PageResult<>(UserFavoritesVO.convertFor(userFavoritesList,CommentHashMap,ThumbHashMap),
                param.getPageNum(),param.getPageSize(),result.getTotalElements());


        return pageResult;


    }


}
