package com.bbs.njtech.posting.param;


import cn.hutool.core.util.StrUtil;
import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.user.domain.UserFavorites;
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
public class UserFavoritesParam extends PageParam {

    private String userId;

    private String postMessageId;

    public Specification<UserFavorites> buildeSpecification(){
        UserFavoritesParam param = this;

        Specification<UserFavorites> spec = new Specification<UserFavorites>() {

            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<UserFavorites> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//                return null;
//                Join<UserFavorites, PostMessage> join = root.join("postMessage");

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("state"), Constant.收藏状态_收藏));
                if (StrUtil.isNotEmpty(param.getUserId())){
                    predicates.add(builder.equal(root.get("userId"),param.getUserId()));
                }



//                if (StrUtil.isNotEmpty(param.getPostMessageId())){
//                    predicates.add(builder.equal(root.get("postMessageId"),param.getPostMessageId()));
//                }

                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) :null;


            }
        };
        return spec;
    }



}
