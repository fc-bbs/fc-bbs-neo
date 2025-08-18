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
public class FindAllMyPostMessageParam extends PageParam {

    public String userId;

    public Specification<PostMessage>buildSpecification(){
        FindAllMyPostMessageParam param = this;
        Specification<PostMessage>spec = new Specification<PostMessage>() {



            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PostMessage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//                return null;
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("userId"),param.getUserId()));

                predicates.add(builder.notEqual(root.get("state"), Constant.帖子状态_已删除));

//                predicates.add(builder.)

                return predicates.size() >0 ?builder.and(predicates.toArray(predicates.toArray(new Predicate[predicates.size()]))):null;

            }

        };
        return spec;
    }




}
