package com.bbs.njtech.user.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.bbs.njtech.common.exception.BizException;
import com.bbs.njtech.common.vo.PageResult;
import com.bbs.njtech.common.vo.TokenInfo;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.setting.repo.SystemSettingRepo;
import com.bbs.njtech.user.domain.*;
import com.bbs.njtech.user.param.*;
import com.bbs.njtech.user.repo.*;
import com.bbs.njtech.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Validated
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CollegeRepo collegeRepo;

    @Autowired
    private UserSubscribeRepo userSubscribeRepo;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserAvatarTempRepo userAvatarTempRepo;

    @Autowired
    private UserNickNameTempRepo userNickNameTempRepo;

    @Autowired
    private SystemSettingRepo systemSettingRepo;

    @Value("${app.appid}")
    private String appid;

    @Value("${app.secret}")
    private String secret;

    @Transactional
    public TokenInfo doLogin(String mobile,String oid) {
        User user = userRepo.findUserByMobile(mobile);
        if (user == null) {//没找到该用户,进行注册
            //注册部分的内容

            String value = redisTemplate.opsForValue().get(oid);
            if(value==null){
                throw new BizException("非法登录");
            }
            if(value.equals("1")){
                List<UserAvatarTemp> userAvatarTempList = userAvatarTempRepo.findAll();
                List<UserNickNameTemp> userNickNameTempList = userNickNameTempRepo.findAll();
                Random rand = new Random();
                String avatarUrl = userAvatarTempList.get((rand.nextInt(userAvatarTempList.size()))).getAvatarUrl();
                String nickName = userNickNameTempList.get((rand.nextInt(userNickNameTempList.size()))).getNickName();
                User newUser = User.register(mobile,oid,avatarUrl,nickName);
                userRepo.save(newUser);
                StpUtil.login(newUser.getId(), new SaLoginModel().setIsLastingCookie(false));
                StpUtil.getSession().set("mobile", mobile);

                TokenInfo tokenInfo = TokenInfo.build();
                tokenInfo.setAccountId(newUser.getId());

                return tokenInfo;
            }
            else {
                throw new BizException("非法登录");
            }

        } else {

            if(user.getOpenId().equals(oid)){
                //这个是登录用户的主键，业务中你要从数据库中读取
                if(user.getDeleteFlag()){//该用户已经被删除
                    throw new BizException("您已被封禁");
                }
                StpUtil.login(user.getId(), new SaLoginModel().setIsLastingCookie(false));
                StpUtil.getSession().set("mobile", mobile);
                //获取登录生成的token
                TokenInfo tokenInfo = TokenInfo.build();
                tokenInfo.setAccountId(user.getId());
                System.out.println(tokenInfo);
                return tokenInfo;
            }
            else throw new BizException("非法登录");

//            //这个是登录用户的主键，业务中你要从数据库中读取
//            StpUtil.login(user.getId(), new SaLoginModel().setIsLastingCookie(false));
//            StpUtil.getSession().set("mobile", mobile);
//            //获取登录生成的token
//            TokenInfo tokenInfo = TokenInfo.build();
//            tokenInfo.setAccountId(user.getId());
//            System.out.println(tokenInfo);
//            return tokenInfo;
        }


    }

    @Transactional
    public UserVO findUserById() {
        User user = userRepo.getReferenceById(StpUtil.getLoginIdAsString());
        return UserVO.convertFor(user);
    }
    @Transactional
    public UserVO findUserByIdWithUnLogin() {
        College college = collegeRepo.getReferenceById("001");
        String avatar = systemSettingRepo.getReferenceById("001").getAppAvatarUrl();

        return UserVO.convertForWithUnLogin(college.getName(),avatar);
    }

    //查询用户所有的关注
    @Transactional
    public PageResult<UserSubscribeVO> findAllSubscribe(UserSubscribeParam param) {
        param.setUserId(StpUtil.getLoginIdAsString());
        Page<UserSubscribe> result = userSubscribeRepo.findAll(param.buildeSpecification(),
                PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
        List<UserSubscribeVO> userSubscribeVOList = new ArrayList<>();
        for (UserSubscribe userSubscribe : result) {
            if (userSubscribe.getState().equals(Constant.关注状态_已关注)){
                userSubscribeVOList.add( UserSubscribeVO.convertFor(userSubscribe,true));
            }
        }
        PageResult<UserSubscribeVO> pageResult = new PageResult<>(userSubscribeVOList,
                param.getPageNum(), param.getPageSize(), result.getTotalElements());

        return pageResult;
    }


    //查询用户所有的粉丝
    @Transactional(readOnly = true)
    public PageResult<UserFansVO> findAllFans(UserSubscribeParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        Page<UserSubscribe> result = userSubscribeRepo.findAll(param.buildSpecificationAboutFans(),
                PageRequest.of(param.getPageNum()-1, param.getPageSize(),Sort.by(Sort.Order.desc("createTime")) ));
        List<UserFansVO> userSubscribeVOList = new ArrayList<>();
        for (UserSubscribe userSubscribe : result){
            userSubscribeVOList.add( UserFansVO.convertFor(userSubscribe));
        }
        PageResult<UserFansVO> pageResult = new PageResult<>(userSubscribeVOList,
                param.getPageNum(),param.getPageSize(),result.getTotalElements());
        return pageResult;
    }

    @Transactional
    public UserVO findOtherUserById(OtherUserParam param){
        User user = userRepo.getReferenceById(param.getUserSubscribeId());
        Boolean subscribeFlag;
        if (userSubscribeRepo.findFirstByUserIdAndUserSubscribeId(param.getUserId(), param.getUserSubscribeId()) == null||
                userSubscribeRepo.findFirstByUserIdAndUserSubscribeId(param.getUserId(), param.getUserSubscribeId()).getState().equals(Constant.关注状态_取关)){
            //如果之前没有点过关注或者显示为取关
            subscribeFlag = false;
        }
        else {
            subscribeFlag = true;
        }
        return UserVO.buildUser(user,subscribeFlag);
    }

    @Transactional
    public UserVO findOtherUserByIdWithUnLogin(OtherUserParam param){
        User user = userRepo.getReferenceById(param.getUserSubscribeId());
        return UserVO.buildUser(user,false);
    }
    @Transactional
    public Boolean updateSubscribeState(SubscribeOtherParam param){
        param.setUserId(StpUtil.getLoginIdAsString());
        UserSubscribe userSubscribe = userSubscribeRepo.findFirstByUserIdAndUserSubscribeId(param.getUserId(), param.getUserSubscriberId());

        if (param.getUserId().equals(param.getUserSubscriberId())){
            throw new BizException("自己不能关注自己");
        }
        if (userSubscribe == null){
            //数据库之前没有记录，前端传值为false
            if (param.getSubscribeState().equals(false)){
                UserSubscribe newUserSubscribe = UserSubscribe.build(param.getUserId(), param.getUserSubscriberId());

                //当前用户的关注数量+1
                User user = userRepo.getReferenceById(param.getUserId());
                user.setSubscribeNumber(user.getSubscribeNumber()+1);
                userRepo.save(user);
                //被关注的用户粉丝数量+1
                User userSub = userRepo.getReferenceById(param.getUserSubscriberId());
                userSub.setFansNumber(userSub.getFansNumber()+1);
                userRepo.save(userSub);

                userSubscribeRepo.save(newUserSubscribe);
                return true;
            }
            else {
                throw new BizException("关注异常!!!");
            }
        }else {
            if (param.getSubscribeState().equals(true)&&userSubscribe.getState().equals(Constant.关注状态_已关注)){
//                UserSubscribe userSubscribe1 = userSubscribeRepo.findFirstByUserIdAndUserSubscribeId(param.getUserId(), userSubscribe.getUserSubscribeId());
                userSubscribe.setState(Constant.关注状态_取关);//取消关注
                userSubscribeRepo.save(userSubscribe);
                //当前用户的关注数量-1
                User user = userRepo.getReferenceById(param.getUserId());
                user.setSubscribeNumber(user.getSubscribeNumber()-1);
                userRepo.save(user);
                //被关注的用户粉丝数量-1
                User userSub = userRepo.getReferenceById(param.getUserSubscriberId());
                userSub.setFansNumber(userSub.getFansNumber()-1);
                userRepo.save(userSub);
                return false;
            } else if (param.getSubscribeState().equals(false)&&userSubscribe.getState().equals(Constant.关注状态_取关)) {
//                UserSubscribe userSubscribe1 = userSubscribeRepo.findFirstByUserIdAndUserSubscribeId(param.getUserId(), userSubscribe.getUserSubscribeId());
                userSubscribe.setState(Constant.关注状态_已关注);//取消关注
                userSubscribeRepo.save(userSubscribe);
                //当前用户的关注数量+1
                User user = userRepo.getReferenceById(param.getUserId());
                user.setSubscribeNumber(user.getSubscribeNumber()+1);
                userRepo.save(user);
                //被关注的用户粉丝数量+1
                User userSub = userRepo.getReferenceById(param.getUserSubscriberId());
                userSub.setFansNumber(userSub.getFansNumber()+1);
                userRepo.save(userSub);
                return true;
            }else {
                throw new BizException("关注信息异常");
            }
        }
//        UserSubscribe userSubscribe = userSubscribeRepo.getReferenceById(param.)
    }
    @Transactional
    public UserOpenidAndSessionKeyVO getOpenidAndSessionKey(UserLoginCodeParam param) throws IOException {

        OkHttpClient client = new OkHttpClient();

        // 构建请求的 URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/sns/jscode2session").newBuilder();
        urlBuilder.addQueryParameter("appid", appid);
        urlBuilder.addQueryParameter("secret", secret);
        urlBuilder.addQueryParameter("js_code", param.getCode());
        urlBuilder.addQueryParameter("grant_type", "authorization_code");
        String url = urlBuilder.build().toString();

        // 构建请求对象
        Request request = new Request.Builder()
                .url(url)
                .build();

        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            // 处理响应
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // 输出响应体
//            System.out.println(response.body().string());
            JSONObject json = JSONObject.parseObject(response.body().string());
            String openid = json.getString("openid");
            String sessionKey = json.getString("session_key");
            //2.28更新往redis存入openId
            redisTemplate.opsForValue().set(openid, "1", 60, TimeUnit.MINUTES);//openid有效时间60分钟




            return UserOpenidAndSessionKeyVO.build(openid,sessionKey,appid);
        }
    }

    //3.2
    @Transactional
    public String updateUserSettingInfo(UpdateUserSettingParam param) {
        User user = userRepo.getReferenceById(param.getId());
        user.setAvatarUrl(param.getAvatarUrl());
        user.setNickName(param.getNickName());
        user.setSignature(param.getSignature());
        user.setGrade(param.getGrade());
        user.setSchool(param.getSchool());
        user.setSex(param.getSex());
        userRepo.save(user);
        return ("个人信息修改成功");
    }
    //3.2
    @Transactional
    public UserSettingVO findUserSettingById() {
        User user = userRepo.getReferenceById(StpUtil.getLoginIdAsString());
        return UserSettingVO.convertFor(user);
    }
    //3.2
    @Transactional
    public String exchangeAvatar() {
        Random rand = new Random();
        List<UserAvatarTemp> userAvatarTemps = userAvatarTempRepo.findAll();
        return  userAvatarTemps.get((rand.nextInt(userAvatarTemps.size()))).getAvatarUrl();
    }

    @Transactional
    public String exchangeNickName() {
        Random rand = new Random();
        List<UserNickNameTemp> userNickNameTempList = userNickNameTempRepo.findAll();
        return  userNickNameTempList.get((rand.nextInt(userNickNameTempList.size()))).getNickName();
    }

}
