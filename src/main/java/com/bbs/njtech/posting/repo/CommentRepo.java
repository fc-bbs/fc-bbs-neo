package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface CommentRepo extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {


    Comment findFirstByPostMessageIdAndDeleteFlagOrderByPublishTimeAsc(String postMessageId,Boolean DeleteFlag);
    List<Comment> findCommentsByPostMessageIdAndDeleteFlag(String postMessageId, Boolean DeleteFlag);
}
