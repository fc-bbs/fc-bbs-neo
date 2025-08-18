package com.bbs.njtech.card.vo;

import com.bbs.njtech.card.domain.UserVipCard;
import lombok.Data;

@Data
public class QRCodeVO {

    private String qrCode;
    private String url;

    public static QRCodeVO build(UserVipCard card, String h5Gateway) {
        QRCodeVO vo= new QRCodeVO();
        vo.setQrCode(card.getQrCode());
        vo.setUrl(h5Gateway +"/#/pages/register/register?userVipCode=" + card.getQrCode());
        return vo;
    }
}
