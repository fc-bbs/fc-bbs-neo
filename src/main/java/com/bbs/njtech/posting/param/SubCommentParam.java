package com.bbs.njtech.posting.param;

import cn.hutool.core.util.StrUtil;
import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.posting.domain.SubComment;
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
public class SubCommentParam extends PageParam {

    private String commentId;


    public Specification<SubComment>buildeSpecification(){
        SubCommentParam param = this;
        Specification<SubComment>spec = new Specification<SubComment>() {


            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<SubComment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("deleteFlag"), false));
                if(StrUtil.isNotEmpty(param.getCommentId())){
                    predicates.add(builder.equal(root.get("commentId"), param.getCommentId()));
                }

                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
            }
        };
        return spec;
    }

}
