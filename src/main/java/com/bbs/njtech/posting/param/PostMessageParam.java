package com.bbs.njtech.posting.param;

import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.posting.domain.PostMessage;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class PostMessageParam extends PageParam {

    private String commentId;

    private String subCategoryName;

    private String subCategoryId;

    private String searchText;

    private Boolean isActivity;

    private String userId;

    private Boolean isService;

    public Specification<PostMessage>buildeSpecification(){
        PostMessageParam param = this;
        Specification<PostMessage>spec = new Specification<PostMessage>() {


            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<PostMessage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("state"), Constant.帖子状态_已发布));

                if (param.getSubCategoryId() != null) {
                    predicates.add(builder.equal(root.get("subCateGoryId"),param.getSubCategoryId()));

                }
                if (param.getSearchText()!=null){
                    predicates.add(builder.like(root.get("postTextContent"), "%" + param.getSearchText() + "%"));
                }

                if(param.getIsActivity()!=null){//是否要求是活动
                    if(param.getIsActivity()){
                        predicates.add(builder.equal(root.get("type"), Constant.帖子类型_活动帖子));
                    }
                    else {
                        predicates.add(builder.equal(root.get("type"), Constant.帖子类型_普通帖子));//
                    }
                }

                if(param.getIsService()!=null){//是否要求是服务
                    if(param.getIsService()){
                        predicates.add(builder.equal(root.get("type"),Constant.帖子类型_服务帖子));
                    }else{
                        predicates.add(builder.equal(root.get("type"),Constant.帖子类型_普通帖子));
                    }
                }

//                if (StrUtil.isNotEmpty(root.get("sub_category_id").toString())){
//                    predicates.add(root.get("sub_category_id").get("id"));
//                    predicates.add(builder.equal(root.get("sub_category_id").get("id"),param.getCommentId()));
//                }
                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
            }
        };
        return spec;
    }

}
