package com.bbs.njtech.posting.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.posting.param.*;
import com.bbs.njtech.posting.service.*;
import com.bbs.njtech.posting.vo.*;
import com.bbs.njtech.user.param.UserFavoritesStateParam;
import com.bbs.njtech.user.service.UserFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postMessage")
public class PostMessageController {

    @Autowired
    private PostMessageService postMessageService;

    @Autowired
    private GeneralCategoryService generalCategoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCommentService subCommentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserFavoritesService userFavoritesService;
    //分页查询所有的帖子
    //目前还需要增加根据用户身份来推送不同学校的帖子
    @GetMapping("/findLatestPostMessageByPage")
    public Result<PageResult<PostMessageVO>> findLatestPostMessageByPage(PostMessageParam param){
        if(StpUtil.isLogin()){//
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findLatestPostMessageByPage(param));
        }else {
            return Result.success(postMessageService.findLatestPostMessageByPageWithUnLogin(param));
        }



    }
    //还没实现,寻找热门帖子
    @GetMapping("/findHotPostMessageByPage")
    public Result<PageResult<PostMessageVO>> findHotPostMessageByPage(PostMessageParam param){

        if(StpUtil.isLogin()){//
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findHotPostMessageByPage(param));
        }else {
            return Result.success(postMessageService.findHotPostMessageByPageWithUnLogin(param));
        }

    }
    @GetMapping("/findActivityPostMessageByPage")
    public Result<PageResult<PostMessageVO>> findActivityPostMessageByPage(PostMessageParam param){
        param.setIsActivity(true);
        if(StpUtil.isLogin()){
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findActivityPostMessageByPage(param));
        }else {
            return Result.success(postMessageService.findActivityPostMessageByPageWithUnLogin(param));
        }

    }
    @GetMapping("/findServicePostMessageByPage")
    public Result<PageResult<PostMessageVO>> findServicePostMessageByPage(PostMessageParam param){
        param.setIsService(true);
        if(StpUtil.isLogin()){
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findServicePostMessageByPage(param));
        }else {

            return Result.success(postMessageService.findServicePostMessageByPageWithUnLogin(param));
        }
    }
    
    @GetMapping("/findPostMessageById")//详细信息
    public Result<PostMessageDetailVO> findPostMessageDetailById(PostMessageDetailParam param){
        if(StpUtil.isLogin()){
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findPostMessageDetailById(param));
        }
        else {
            System.out.println("1");
            return Result.success(postMessageService.findPostMessageDetailByIdWithUnLogin(param));
        }

    }

    //根据用户学校找到大类
    @GetMapping("/findGeneralCategory")//用于根据collegeId找到所有的GeneralCategory
    public Result<List<GeneralCategoryVO>> findGeneralCategory(){
        return Result.success(generalCategoryService.findGeneralCategory(StpUtil.getLoginIdAsString()));
    }
    //1.11增加代码：根据用户学校找到小类
//    @GetMapping("/findSubCategory")//没实现
//    public Result<List<SubCategoryVO>> findSubCategory(){
//
//        return Result.success(subCategoryService.findSubCategory(StpUtil.getLoginIdAsString()));
//    }

    @GetMapping("/findCategory")
    public Result<CategoryVO> findCategory(){
        return Result.success(postMessageService.findCategory());
    }
    //新增发帖功能
    @PostMapping("/addPostMessage")
    @ResponseBody
    public Result<String> addPostMessage(PostMessageContentParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        postMessageService.addPostMessage(param);
        return Result.success();
    }

    //1.23新增查询某个帖子的评论代码

    @GetMapping("/findCommentByPage")
    public Result<PageResult<CommentVO>> findCommentByPage(CommentParam param){

        return Result.success(commentService.findCommentByPage(param));
    }
    @GetMapping("/findAllSubCommentByCommentId")
    public Result<List<SubCommentVO>> findAllSubCommentByCommentId(SubCommentParam param){

        return Result.success(subCommentService.findAllSubCommentByCommentId(param));
    }

    //新增查找用户发过的所有的帖子
    @GetMapping("/findAllMyPostMessage")
    public Result<PageResult<MyPostMessageVO>> findAllMyPostMessageByPage(FindAllMyPostMessageParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        return Result.success(postMessageService.findAllMyPostMessageByPage(param));

    }

    @GetMapping("/findAllOtherPostMessage")
    public Result<PageResult<MyPostMessageVO>> findAllOtherPostMessageByPage(FindAllOtherPostMessageParam param){
        return Result.success(postMessageService.findAllOtherPostMessageByPage(param));
    }
    @GetMapping("/findCommentById")
    public Result<CommentContentVO> findCommentById(String commentId){
        return Result.success(commentService.findCommentById(commentId));

    }

    @GetMapping("/deletePostMessage")
    public Result<String> deletePostMessage(PostMessageDeleteParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        postMessageService.deletePostMessage(param);
        return Result.success();

    }
    @PostMapping("/updateFavoritesState")
    public Result<Boolean> updateFavoritesState(UserFavoritesStateParam param){
        param.setUserId(StpUtil.getLoginIdAsString());//目前先指定一个UserId
        return Result.success(userFavoritesService.updateFavoritesState(param));
    }

    @GetMapping("/findAllUserFavorites")
    public Result<PageResult<UserFavoritesVO>> findAllUserFavorites(UserFavoritesParam param){
        return Result.success(userFavoritesService.findAllUserFavorites(param));
    }

    @PostMapping("/commentPostMessage")//评论帖子
    public Result<String> commentPostMessage(CommentPostMessageContentParam param){
        param.setUserId(StpUtil.getLoginIdAsString());//目前先指定一个UserId
        commentService.commentPostMessage(param);
        return Result.success();
    }
    @GetMapping("/findAllMyCommentByPage")
    public Result<PageResult<MyCommentVO>> findAllMyCommentByPage(FindAllMyCommentParam param){
        if(param.getUserId()==null){
            param.setUserId(StpUtil.getLoginIdAsString());
        }
        return Result.success(commentService.findAllMyCommentByPage(param));
    }

    @GetMapping("/deleteComment")
    public Result<String> deleteComment(CommentDeleteParam param){
        commentService.deleteComment(param);
        return Result.success();
    }
    @GetMapping("/deleteSubComment")
    public Result<String> deleteSubComment(SubCommentDeleteParam param){
        subCommentService.deleteSubComment(param);
        return Result.success();
    }
    @PostMapping("/commentToComment")//
    public Result<String> commentToComment(CommentToCommentContentParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        subCommentService.commentToComment(param);
        return Result.success();
    }
    @PostMapping("/commentToSubComment")
    public Result<String> commentToSubComment(CommentToSubCommentContentParam param){
        param.setUserId(StpUtil.getLoginIdAsString());//目前先指定一个UserId
        subCommentService.commentToSubComment(param);
        return Result.success();
    }
    //    中文模糊查询或者根据子类查询
    @GetMapping("/findPostMessageBySubcategoryOrBySearchTextByPage")
    public Result<PageResult<PostMessageVO>> findPostMessageBySubcategoryOrBySearchTextByPage(PostMessageParam param){

        if (StpUtil.isLogin()){
            param.setUserId(StpUtil.getLoginIdAsString());
            return Result.success(postMessageService.findPostMessageBySubcategoryOrBySearchTextByPage(param));
        }else {
            return Result.success(postMessageService.findPostMessageBySubcategoryOrBySearchTextByPageWithUnLogin(param));
        }



    }

    @GetMapping("/findMobileByPostMessageId")
    public Result<String> findMobileByPostMessageId(PostMessageUserMobileParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        return Result.success().setData(postMessageService.findMobileByPostMessageId(param));
    }

    @GetMapping("/postMessageDetectEffective")////检测这个帖子状态是否正常
    public Result<Boolean> postMessageDetectEffective(PostMessageDetectEffectiveParam param) {
            return Result.success( postMessageService.postMessageDetectEffective(param));
    }

}
