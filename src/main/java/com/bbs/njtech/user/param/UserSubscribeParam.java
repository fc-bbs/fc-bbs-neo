package com.bbs.njtech.user.param;

import cn.hutool.core.util.StrUtil;
import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.user.domain.UserSubscribe;
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
public class UserSubscribeParam extends PageParam {
    private String userId;




    public Specification<UserSubscribe>buildeSpecification(){
        UserSubscribeParam param = this;

        Specification<UserSubscribe>spec = new Specification<UserSubscribe>() {

            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<UserSubscribe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("state"), Constant.关注状态_已关注)) ;
                if (StrUtil.isNotEmpty(param.getUserId())){
                    predicates.add(builder.equal(root.get("userId"),param.getUserId()));
                }

                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
            }

        };
        return spec;

    }


    public Specification<UserSubscribe>buildSpecificationAboutFans(){
        UserSubscribeParam param = this;
        Specification<UserSubscribe>spec = new Specification<UserSubscribe>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<UserSubscribe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("state"), Constant.关注状态_已关注)) ;
                if (StrUtil.isNotEmpty(param.getUserId())){
                    predicates.add(builder.equal(root.get("userSubscribeId"),param.getUserId()));
                }

                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
            }

        };
        return spec;
    }





}
