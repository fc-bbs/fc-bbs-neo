package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.Thumb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ThumbRepo extends JpaRepository<Thumb,String>, JpaSpecificationExecutor<Thumb> {

    Thumb findFirstByUserIdAndPostMessageId(String userId,String postMessageId);


}
