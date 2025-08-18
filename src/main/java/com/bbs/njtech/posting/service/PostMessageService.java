package com.bbs.njtech.posting.service;


import com.alibaba.fastjson.JSONObject;
import com.bbs.njtech.WxAPI.Service.WxSaveService;
import com.bbs.njtech.WxAPI.Service.WxUrlService;
import com.bbs.njtech.WxAPI.domain.TextCheckResponse;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.message.domain.Message;
import com.bbs.njtech.message.repo.MessageRepo;
import com.bbs.njtech.posting.domain.*;
import com.bbs.njtech.posting.param.*;
import com.bbs.njtech.posting.repo.*;
import com.bbs.njtech.posting.vo.CategoryVO;
import com.bbs.njtech.posting.vo.MyPostMessageVO;
import com.bbs.njtech.posting.vo.PostMessageDetailVO;
import com.bbs.njtech.posting.vo.PostMessageVO;
import com.bbs.njtech.user.domain.User;
import com.bbs.njtech.user.domain.UserFavorites;
import com.bbs.njtech.user.repo.UserFavoritesRepo;
import com.bbs.njtech.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Validated
@Service
@Slf4j
public class PostMessageService {
//发帖服务
    @Autowired
    private PostMessageRepo postMessageRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ThumbRepo thumbRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GeneralCategoryRepo generalCategoryRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @Autowired
    private UserFavoritesRepo userFavoritesRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private SubCommentRepo subCommentRepo;

    @Autowired
    private WxSaveService wxSaveService;

    @Autowired
    private WxUrlService wxUrlService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${wx-img-check}")
    private String wxImgCheck;

    @Value("${wx-text-check}")
    private String wxTextCheck;
    @Value("${wx-generate-UrlLink}")
    private String wxGenerateUrlLink;
    @Transactional(readOnly = true)//找最近帖子
    public PageResult<PostMessageVO>findLatestPostMessageByPage(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
          if(!postMessage.getCommentNumber().equals(0)){//有评论
              Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
              CommentHashMap.put(postMessage.getId(),comment);
          }
          if(!postMessage.getThumbNumber().equals(0)){//有点赞
              Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
              if(thumb!=null){//有记录
                  if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                      ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_已点赞);
                  }
                  else ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
              }else{
                  ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
              }

          }
          if(!postMessage.getFavoritesNumber().equals(0)){
              UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
              if(userFavorites!=null){//有记录
                  if(userFavorites.getState().equals(Constant.收藏状态_收藏)){//是点赞
                      FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_收藏);
                  }
                  else FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
              }else{
                  FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
              }
          }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }
    @Transactional(readOnly = true)//找最近帖子
    public PageResult<PostMessageVO>findLatestPostMessageByPageWithUnLogin(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }

    @Transactional(readOnly = true)//找热门帖子
    public PageResult<PostMessageVO>findHotPostMessageByPage(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("hotPoint") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
            if(!postMessage.getThumbNumber().equals(0)){//有点赞
                Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(thumb!=null){//有记录
                    if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                        ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_已点赞);
                    }
                    else ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }else{
                    ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }

            }
            if(!postMessage.getFavoritesNumber().equals(0)){
                UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(userFavorites!=null){//有记录
                    if(userFavorites.getState().equals(Constant.收藏状态_收藏)){//是点赞
                        FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_收藏);
                    }
                    else FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }else{
                    FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }

    @Transactional(readOnly = true)//找热门帖子
    public PageResult<PostMessageVO>findHotPostMessageByPageWithUnLogin(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("hotPoint") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
        }
        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }

    @Transactional(readOnly = true)//找活动帖子
    public PageResult<PostMessageVO>findActivityPostMessageByPage(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
            if(!postMessage.getThumbNumber().equals(0)){//有点赞
                Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(thumb!=null){//有记录
                    if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                        ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_已点赞);
                    }
                    else ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }else{
                    ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }

            }
            if(!postMessage.getFavoritesNumber().equals(0)){
                UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(userFavorites!=null){//有记录
                    if(userFavorites.getState().equals(Constant.收藏状态_收藏)){//是点赞
                        FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_收藏);
                    }
                    else FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }else{
                    FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }
    @Transactional(readOnly = true)//找活动帖子
    public PageResult<PostMessageVO>findActivityPostMessageByPageWithUnLogin(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }
    @Transactional(readOnly = true)
    public  PageResult<PostMessageVO>findServicePostMessageByPage(PostMessageParam param){

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
            if(!postMessage.getThumbNumber().equals(0)){//有点赞
                Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(thumb!=null){//有记录
                    if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                        ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_已点赞);
                    }
                    else ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }else{
                    ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }

            }
            if(!postMessage.getFavoritesNumber().equals(0)){
                UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(userFavorites!=null){//有记录
                    if(userFavorites.getState().equals(Constant.收藏状态_收藏)){//是点赞
                        FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_收藏);
                    }
                    else FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }else{
                    FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );


    }

    @Transactional(readOnly = true)//找服务
    public PageResult<PostMessageVO>findServicePostMessageByPageWithUnLogin(PostMessageParam param){//

        Sort sort = Sort.by(
                Sort.Order.desc("top"), // 先按照 top 属性降序排序
                Sort.Order.desc("postTime") // 再按照 postTime 属性降序排序
        );
        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), sort));

        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
        }
        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }

    @Transactional
    public void addPostMessage(PostMessageContentParam param){
        User user = userRepo.getReferenceById(param.getUserId());
        if(user.getDeleteFlag()){//该用户已经被封禁
            throw new BizException("您已被封禁,无法发帖");
        }
        PostMessage postMessage = param.convertToPo();//创建一个postMessage，并且给他赋值
        SubCategory subCategory = subCategoryRepo.findByNameAndDeleteFlagFalse(param.getSubCategoryName());
        if(subCategory.getGeneralCategory().getIsService()){
            throw new BizException("您不能在该板块填写帖子哦");
        }
        postMessage.setSubCateGoryId(subCategory.getId());
        if(subCategory.getGeneralCategory().getIsActivity()){
            postMessage.setType(Constant.帖子类型_活动帖子);
        }
        //1.18修改，设置初值为0

        postMessageRepo.save(postMessage);
    }

    @Transactional(readOnly = true)
    public CategoryVO findCategory(){
        List<GeneralCategory> generalCategoryList = generalCategoryRepo.findByDeleteFlagFalse();//查找非活动类型的帖子
        List<SubCategory> subCategoryList = subCategoryRepo.findByDeleteFlagFalse();
        return CategoryVO.build(generalCategoryList,subCategoryList);
    }

    @Transactional(readOnly = true)
    public PostMessageDetailVO findPostMessageDetailById(PostMessageDetailParam param){
        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
        Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),param.getPostMessageId());
        UserFavorites favorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),param.getPostMessageId());
        return PostMessageDetailVO.convertFor(postMessage,thumb,favorites);
    }

    @Transactional(readOnly = true)
    public PostMessageDetailVO findPostMessageDetailByIdWithUnLogin(PostMessageDetailParam param){
        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
        return PostMessageDetailVO.convertFor(postMessage,null,null);
    }
    @Transactional(readOnly = true)
    public PageResult<MyPostMessageVO>  findAllMyPostMessageByPage(FindAllMyPostMessageParam param){

        Page<PostMessage> result = postMessageRepo.findAll(param.buildSpecification(),
             PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("postTime"))));
        PageResult<MyPostMessageVO> pageResult = new PageResult<>(MyPostMessageVO.convertFor(
                result.getContent()),param.getPageNum(),param.getPageSize(),result.getTotalElements());

        return pageResult;
    }

    @Transactional(readOnly = true)
    public PageResult<MyPostMessageVO> findAllOtherPostMessageByPage(FindAllOtherPostMessageParam param){
        Page<PostMessage> result = postMessageRepo.findAll(param.buildSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("postTime"))));
        PageResult<MyPostMessageVO> pageResult = new PageResult<>(MyPostMessageVO.convertFor(
                result.getContent()),param.getPageNum(),param.getPageSize(),result.getTotalElements());

        return pageResult;
    }

//    @Transactional
//    public void  deletePostMessage(PostMessageDeleteParam param){
//        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
//        if(postMessage.getUserId().equals(param.getUserId())){
//            postMessage.setState(Constant.帖子状态_已删除);
//            postMessageRepo.save(postMessage);
//        }else{
//            throw new BizException("该帖子不能由非发帖人删除");
//        }
//
//    }
    @Transactional
    public void  deletePostMessage(PostMessageDeleteParam param){
        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
        if(postMessage.getUserId().equals(param.getUserId())){
            //删掉帖子
            postMessage.setState(Constant.帖子状态_已删除);

            List<Comment> commentList = commentRepo.findCommentsByPostMessageIdAndDeleteFlag(postMessage.getId(), false);
            //删掉评论
            for (Comment comment :commentList){
                List<SubComment> subCommentList = subCommentRepo.findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(comment.getId(),false);
                for (SubComment subComment : subCommentList){

                    //删掉评论的评论
                    List<SubComment> toSubCommentList = subCommentRepo.findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(subComment.getCommentId(),false);

                    for (SubComment toSubComment:toSubCommentList){
                        toSubComment.setDeleteFlag(true);

                        toSubComment.setDeleteTime(new Date());
                        subCommentRepo.save(toSubComment);
                    }

                    subComment.setDeleteFlag(true);

                    subCommentRepo.save(subComment);
                }
                comment.setDeleteFlag(true);
                comment.setDeleteTime(new Date());
                comment.setSubCommentNumber(comment.getSubCommentNumber()-subCommentList.size());
                commentRepo.save(comment);
            }
            postMessage.setDeleteTime(new Date());
            postMessage.setCommentNumber(postMessage.getCommentNumber() - commentList.size());
            postMessageRepo.save(postMessage);
        }else{
            throw new BizException("该帖子不能由非发帖人删除");
        }

    }


    @Transactional(readOnly = true)
    public PageResult<PostMessageVO>findPostMessageBySubcategoryOrBySearchTextByPage(PostMessageParam param){

        if(!param.getSubCategoryName().isEmpty()){//
            SubCategory subCategory = subCategoryRepo.findByNameAndDeleteFlagFalse(param.getSubCategoryName());
            param.setSubCategoryId(subCategory.getId());
            if(subCategory.getGeneralCategory().getIsActivity()){
                param.setIsActivity(true);
            }
            if(subCategory.getGeneralCategory().getIsService()){
                param.setIsService(true);
            }

        }


        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("postTime"))));
        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
            if(!postMessage.getThumbNumber().equals(0)){//有点赞
                Thumb thumb = thumbRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(thumb!=null){//有记录
                    if(thumb.getState().equals(Constant.点赞状态_已点赞)){//是点赞
                        ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_已点赞);
                    }
                    else ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }else{
                    ThumbHashMap.put(postMessage.getId(),Constant.点赞状态_未点赞);
                }

            }
            if(!postMessage.getFavoritesNumber().equals(0)){
                UserFavorites userFavorites = userFavoritesRepo.findFirstByUserIdAndPostMessageId(param.getUserId(),postMessage.getId());
                if(userFavorites!=null){//有记录
                    if(userFavorites.getState().equals(Constant.收藏状态_收藏)){//是点赞
                        FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_收藏);
                    }
                    else FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }else{
                    FavortiesHashMap.put(postMessage.getId(),Constant.收藏状态_取消收藏);
                }
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }


    @Transactional(readOnly = true)
    public PageResult<PostMessageVO>findPostMessageBySubcategoryOrBySearchTextByPageWithUnLogin(PostMessageParam param){

        if(!param.getSubCategoryName().isEmpty()){//
            SubCategory subCategory = subCategoryRepo.findByNameAndDeleteFlagFalse(param.getSubCategoryName());
            param.setSubCategoryId(subCategory.getId());
            if(subCategory.getGeneralCategory().getIsActivity()){
                param.setIsActivity(true);
            }
            if(subCategory.getGeneralCategory().getIsService()){
                param.setIsService(true);
            }

        }

        Page<PostMessage> result = postMessageRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum()-1,param.getPageSize(), Sort.by(Sort.Order.desc("postTime"))));
        List<PostMessage> postMessageList = result.getContent();
        HashMap<String, Comment> CommentHashMap = new HashMap<String, Comment>();//用于存储第一条评论
        HashMap<String, String> ThumbHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        HashMap<String, String> FavortiesHashMap = new HashMap<String, String>();//用于存储是否有点赞的信息
        for(PostMessage postMessage : postMessageList){
            if(!postMessage.getCommentNumber().equals(0)){//有评论
                Comment comment=commentRepo.findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(postMessage.getId(),false);
                CommentHashMap.put(postMessage.getId(),comment);
            }
        }


        return new PageResult<>(PostMessageVO.convertFor(postMessageList,CommentHashMap,ThumbHashMap,FavortiesHashMap),
                param.getPageNum(), param.getPageSize(), result.getTotalElements() );

    }

    @Transactional(readOnly = true)
    public String findMobileByPostMessageId(PostMessageUserMobileParam param){
        PostMessage postMessage  = postMessageRepo.getReferenceById(param.getPostMessageId());
        Date now = new Date();
        if(postMessage.getMobileDeadlineTime()!=null){
            if(postMessage.getMobileDeadlineTime().after(now)){
                return postMessage.getUserMobile();
            }else{
                throw new BizException("联系方式有效时间已过");
            }
        }
        else {
            throw new BizException("没有设置截止日期");
        }


    }

    @Transactional(readOnly = true)
    public Boolean postMessageDetectEffective(PostMessageDetectEffectiveParam param){
        PostMessage postMessage = postMessageRepo.getReferenceById(param.getPostMessageId());
        if(postMessage.getState().equals(Constant.帖子状态_已发布)){//检测这个帖子状态是否正常
            return true;
        }
        else {
            return false;
        }
    }
    @Transactional
    public void calculateHotPoint(){//
        List<PostMessage> postMessageList = postMessageRepo.findAllByState(Constant.帖子状态_已发布);
        for(PostMessage postMessage:postMessageList){
            Integer point = postMessage.getThumbNumber() + 2*postMessage.getFavoritesNumber()+3*postMessage.getCommentNumber();
            postMessage.setHotPoint(point);
            postMessageRepo.save(postMessage);
        }
    }
    @Transactional
    public void auditPostMessage(){//安全审核、添加短链
        List<PostMessage> postMessageList = postMessageRepo.findAllByState(Constant.帖子状态_待审核);




        for(PostMessage postMessage:postMessageList){
            User user = postMessage.getUser();
            //如果用户的openId是空的，那说明这个用户是系统弄的。
            if(user.getOpenId()==null){
                if(wxGenerateUrlLink.equals("true")){
                    String urlLink = wxUrlService.getGenerateUrlLink(postMessage.getId());
                    updateWxUrlLink(urlLink,postMessage);
                }

                postMessage.setState(Constant.帖子状态_已发布);
                postMessageRepo.save(postMessage);
                log.info(postMessage.getId()+"帖子审核通过");
                continue;
            }

            //图片检测开始
            if(wxImgCheck.equals("true")){//打开图片检测
                log.info("帖子微信图像安全模块接口打开");
                String postImageUrl = postMessage.getPostImageUrl();
                if(postImageUrl!=null&& !postImageUrl.isEmpty()){//先查看这个帖子有没有图片
                    log.info(postMessage.getId()+"帖子有图片");
                    String[] imgUrlList = postImageUrl.split(",");
                    int imgListLength = imgUrlList.length;
                    if(Boolean.TRUE.equals(redisTemplate.hasKey("PostMessageImgDetect" + postMessage.getId()))){//查看redis中是否存储了这个帖子信息。
                        log.info("该帖子相关信息已经从redis查询到:"+"PostMessageImgDetect" + postMessage.getId()+":"
                                +redisTemplate.opsForValue().get("PostMessageImgDetect"+postMessage.getId())
                        );

                        String imgNumber = redisTemplate.opsForValue().get("PostMessageImgDetect"+postMessage.getId());//存进去(PostMessageImgDetectxxxxx,图片数量)
                        if(imgNumber.equals("999")){//图片有问题,审核不通过
                            log.info(postMessage.getId()+"帖子的图片有问题,审核不通过");
                            postMessage.setState(Constant.帖子状态_审核不通过);
                            postMessageRepo.save(postMessage);
                            Message message = Message.buildWithPostMessageNotPass(postMessage);
                            messageRepo.save(message);
                            redisTemplate.delete("PostMessageImgDetect"+postMessage.getId());
                            continue;
                        }
                        else if(imgNumber.equals("0")){
                            log.info(postMessage.getId()+"帖子的图片无问题");
                            redisTemplate.delete("PostMessageImgDetect"+postMessage.getId());
                            //说明图片检测没啥问题。那就进入到下面的文字检测
                        }
                        else {
                            log.info(postMessage.getId()+"帖子的图片未检测完");
                            //说明图片还没完全检测完。还需要再等
                            continue;
                        }
                    }else {//redis没存储该帖子图片存储信息。往redis里存储信息，并且跳到下一个。
                        log.info(postMessage.getId()+"帖子在redis中未存储图像的监测信息");
                        log.info("redis中存储该帖子图像的监测信息中---");
                        redisTemplate.opsForValue().set("PostMessageImgDetect"+postMessage.getId(), Integer.toString(imgListLength));//存进去(PostMessageImgDetectxxxxx,图片数量)
                        log.info("帖子在redis中存储图像的监测信息完成！\n"+"PostMessageImgDetect"+postMessage.getId()+":"+imgListLength);
                        //发送图片进行安全检测
                        for(String imgUrl:imgUrlList){
                            log.info(postMessage.getId()+"图片检测请求已经发出");
                            wxSaveService.imgCheck(imgUrl,postMessage.getUser(),postMessage.getId());
                        }
                        //
                        continue;
                    }
                }
            }
            //图片检测结束



            //文本检测开始
            if(wxTextCheck.equals("true")){
                log.info("帖子微信文本安全模块接口打开");
                //文本安全内容审核
                TextCheckResponse textCheckResponse = wxSaveService.postMessageTextCheck(postMessage.getPostTextContent(),postMessage.getUser());
                if(textCheckResponse==null){
                    log.error(postMessage.getId()+"帖子微信安全模块检测网路异常");
                }
                else {
                    if(textCheckResponse.getErrcode().equals(0)){//
                        //如果错误码正常
                        if(textCheckResponse.getResult().getSuggest().equals("risky")){//文本内容明显有问题
                            postMessage.setState(Constant.帖子状态_审核不通过);
                            postMessageRepo.save(postMessage);
                            //需要给用户发送消息，表示审核不通过
                            Message message = Message.buildWithPostMessageNotPass(postMessage);
                            messageRepo.save(message);
                            //
                        }
                        else if(textCheckResponse.getResult().getSuggest().equals("review")){//文本内容可能有问题
                            //交给管理员审核。系统不审核
                        }
                        else {//通过
                            //加入短链

                            if(wxGenerateUrlLink.equals("true")){
                                String urlLink = wxUrlService.getGenerateUrlLink(postMessage.getId());
                                updateWxUrlLink(urlLink,postMessage);
                            }

                            postMessage.setState(Constant.帖子状态_已发布);
                            postMessageRepo.save(postMessage);
                            log.info(postMessage.getId()+"帖子审核通过");
                        }
                    }
                    else {
                        log.error(postMessage.getId()+"帖子微信安全模块接口错误码异常");
                    }
                }
            }else {//不进行文本检测

                log.info("帖子微信文本安全模块接口不打开");
                if(wxGenerateUrlLink.equals("true")){
                    String urlLink = wxUrlService.getGenerateUrlLink(postMessage.getId());
                    updateWxUrlLink(urlLink,postMessage);
                }
                postMessage.setState(Constant.帖子状态_已发布);
                postMessageRepo.save(postMessage);
                log.info(postMessage.getId()+"帖子审核通过");
            }
            //文本检测结束

        }




    }

    @Transactional
    public void updateWxUrlLink(String url,PostMessage postMessage){//更新帖子短链url

        if(url==null){
            log.error("微信短链模块接口异常");
        }else {
            postMessage.setWxUrlLink(url);
            postMessageRepo.save(postMessage);
        }

    }
    @Transactional
    public void updateAllPostMessageWxUrlLink(){//更新帖子短链url
        if(wxGenerateUrlLink.equals("true")){//只有打开开关才会上短链
            List<PostMessage> postMessageList = postMessageRepo.findByWxUrlLinkIsNull();
            for(PostMessage postMessage :postMessageList){

                if(postMessage.getState().equals(Constant.帖子状态_已发布)||
                        postMessage.getState().equals(Constant.帖子状态_待审核)){
                    log.info("查询到id为"+postMessage.getId()+"没有短链");
                    String urlLink = wxUrlService.getGenerateUrlLink(postMessage.getId());
                    updateWxUrlLink(urlLink,postMessage);
                    log.info("补添帖子"+postMessage.getId()+"增加微信链接");
                }


            }
        }

    }

    @Transactional
    public void imgCallbackCheck(JSONObject jsonObject){//图片安全检测的回调请求处理

        JSONObject result = jsonObject.getJSONObject("result");
        String traceId = jsonObject.getString("trace_id");
        String suggest = result.getString("suggest");
        String postMessageId = redisTemplate.opsForValue().get(traceId);
        Integer number = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get("PostMessageImgDetect" + postMessageId)));

        if(number.equals(999)){
            return;
        }
        if(suggest.equals("pass")){
            log.info("该图片无问题，回调请求检测通过");
            number =number-1;
            redisTemplate.opsForValue().set("PostMessageImgDetect" + postMessageId, Integer.toString(number));
        }
        else if(suggest.equals("review")){
            log.info("该图片可能有问题，设置为999");
            redisTemplate.opsForValue().set("PostMessageImgDetect" + postMessageId, "999");
        }
        else {//不通过
            log.info("该图片一定有问题，设置为999");
            redisTemplate.opsForValue().set("PostMessageImgDetect" + postMessageId, "999");
        }
    }


}
