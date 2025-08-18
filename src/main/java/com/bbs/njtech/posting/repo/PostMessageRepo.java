package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.PostMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostMessageRepo extends JpaRepository<PostMessage,String>, JpaSpecificationExecutor<PostMessage> {

    List<PostMessage> findAllByState(String state);

    List<PostMessage> findByWxUrlLinkIsNull();

}
