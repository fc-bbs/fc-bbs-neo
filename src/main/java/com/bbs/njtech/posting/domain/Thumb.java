package com.bbs.njtech.posting.domain;

import com.bbs.njtech.common.utils.IdUtils;
import com.bbs.njtech.constants.Constant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Setter
@Getter
@Table(name = "thumb")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class Thumb  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "user_id",length = 32)
    private String userId;

    @Column(name = "post_message_id",length = 32)
    private String postMessageId;

    @Column(name = "state",length = 255)
    private String state;


    public static Thumb build(String userId,String postMessageId){
        Thumb po = new Thumb();
        po.setId(IdUtils.getId());
        po.setUserId(userId);
        po.setPostMessageId(postMessageId);
        po.setState(Constant.点赞状态_已点赞);

        return po;
    }
    public void doThumb(){
       this.setState(Constant.点赞状态_已点赞);

    }
    public void cancelThumb(){
        this.setState(Constant.点赞状态_未点赞);

    }

}
