package com.bbs.njtech.message.param;

import com.bbs.njtech.common.param.PageParam;
import com.bbs.njtech.constants.Constant;
import com.bbs.njtech.message.domain.Message;
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
public class MessageParam extends PageParam {

    private String userId;


    public Specification<Message>buildeSpecification(){
        MessageParam param = this;
        Specification<Message>spec = new Specification<Message>() {


            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate>predicates = new ArrayList<>();
                predicates.add(builder.notEqual(root.get("state"), Constant.消息状态_已删除));//不显示已删除的

                predicates.add(builder.equal(root.get("userId"), userId));//不显示已删除的



                return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
            }
        };
        return spec;
    }

}
