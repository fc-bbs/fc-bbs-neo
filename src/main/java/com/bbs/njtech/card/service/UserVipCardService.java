package com.bbs.njtech.card.service;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.bbs.njtech.card.domain.UserVipCard;
import com.bbs.njtech.card.repo.UserVipCardRepo;
import com.bbs.njtech.card.vo.QRCodeVO;
import com.bbs.njtech.card.vo.UserVipCardVO;
import com.bbs.njtech.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Random;

@Validated
@Service
public class UserVipCardService {

    @Autowired
    private UserVipCardRepo UserVipCardRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional(readOnly = true)
    public UserVipCardVO findUserVip(String userId){
        //User user = userRepo.getReferenceById(userId);
        UserVipCard card = UserVipCardRepo.findUserVipCardByUserId(userId);
        if(card!=null)
            return UserVipCardVO.build(card);
        else
            return null;
    }

    @Transactional
    public void registerUserVip(String userId){
        //User user = userRepo.getReferenceById(userId);
        UserVipCard card = UserVipCard.build(userId);
        UserVipCardRepo.save(card);
    }
    public static String randomCode(){
        int length = 9; // 固定长度
        char[] characters = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length);
            sb.append(characters[index]);
        }
        String randomString = sb.toString();
        System.out.println(randomString);
        return randomString;
    }

    @Transactional(readOnly = true)//二维码
    public ResponseEntity<ByteArrayResource> findUserVipQR(String userId){
        UserVipCard card = UserVipCardRepo.findUserVipCardByUserId(userId);
        if(card!=null) {
            QRCodeVO vo = QRCodeVO.build(card, getH5Gateway());
            MediaType mediaType = MediaType.parseMediaType("image/png");
            System.out.println(vo.getUrl());
            byte[] png = QrCodeUtil.generatePng(vo.getUrl(), 128, 128);
            ByteArrayResource resource = new ByteArrayResource(png);
            return ResponseEntity.ok().contentType(mediaType).body(resource);
        }
        else
            return null;
    }
    @Transactional
    public String getH5Gateway() {
        return "http://localhost:8084";
    }

//    @Transactional
//    public void addMembershipCard(AddPostMessageParam param){
//        PostMessage postMessage = new PostMessage();
//        postMessage.setId(IdUtils.getId());
//        if (StrUtil.isBlank(param.getPostTextContext()) && StrUtil.isBlank(param.getPostImageUrl())){
//            throw new BizException("帖子内容不得为空");
//        }
//        postMessage.setPostTextContent(param.getPostTextContext());
//        //后续可能要加上拼接图片的Url
//        postMessage.setPostImageUrl(param.getPostImageUrl());
//
//        //发帖时间为当前的系统时间
//        postMessage.setPostTime(new Date());
//        postMessage.setDeleteFlag(false);
//        postMessage.setSubCateGoryId(postMessage.getSubCateGoryId());
//
//        //向数据库传入当前发贴用户的Id
//        postMessage.setUserId(param.getUserId());
//
//        postMessageRepo.save(postMessage);
//    }

}
