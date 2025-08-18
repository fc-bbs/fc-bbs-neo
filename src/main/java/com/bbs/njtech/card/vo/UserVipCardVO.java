package com.bbs.njtech.card.vo;

import com.bbs.njtech.card.domain.UserVipCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class UserVipCardVO {//会员信息
    private String id;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date expirationTime;

    private String level;
    private String memberPoint;
    private String detail;
    private String nickName;
    private String memberUrl;//会员图

    public static UserVipCardVO build(UserVipCard card) {
        UserVipCardVO vo= new UserVipCardVO();
        BeanUtils.copyProperties(card,vo);
        vo.setNickName(card.getUser().getNickName());
        return vo;
    }
}
