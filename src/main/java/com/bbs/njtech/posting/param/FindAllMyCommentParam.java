package com.bbs.njtech.posting.param;

import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.posting.domain.Comment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class FindAllMyCommentParam extends PageParam {
    public String userId;

    public Specification<Comment> buildSpecification(){

        FindAllMyCommentParam param = this;
        Specification<Comment>spec = new Specification<Comment>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("userId"),param.getUserId()));

                predicates.add(builder.equal(root.get("deleteFlag"), false));


                return predicates.size() >0 ?builder.and(predicates.toArray(predicates.toArray(new Predicate[predicates.size()]))):null;

            }
        };
        return spec;
    }

}
