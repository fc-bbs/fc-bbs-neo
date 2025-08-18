package com.bbs.njtech.message.repo;

import com.bbs.njtech.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface MessageRepo extends JpaRepository<Message,String>, JpaSpecificationExecutor<Message> {
    Integer countByUserIdAndState(String userId,String state);
}

