package com.bbs.njtech.posting.domain;

import com.bbs.njtech.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Table(name = "post_message")
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
public class PostMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "user_id", length = 32)
    private  String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false, insertable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    private Date postTime;

    @Column(name = "post_image_url", columnDefinition = "TEXT")
    private String postImageUrl;

    private String state;

    @Column(name = "sub_category_id", length = 32)
    private String subCateGoryId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", insertable = false, updatable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubCategory subCategory;

    private Integer commentNumber;

    private Integer thumbNumber;

    private Date deleteTime;

    @Column(name = "post_text_content", length = 500)
    private String postTextContent;

    private String userMobile;

    private Date mobileDeadlineTime;

    private Integer hotPoint;

    private Integer favoritesNumber;

    private String wxUrlLink;


    private String type;
    private Boolean top;//是否置顶
    public  void doThumb( ){

        this.setThumbNumber(this.getThumbNumber()+1);

    }

    public  void cancelThumb(){
        this.setThumbNumber(this.getThumbNumber()-1);

    }

    public  void doFavorite( ){
        this.setFavoritesNumber(this.getFavoritesNumber()+1);

    }

    public  void cancelFavorite(){
        this.setFavoritesNumber(this.getFavoritesNumber()-1);

    }

}
