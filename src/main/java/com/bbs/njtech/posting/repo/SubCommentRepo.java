package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SubCommentRepo extends JpaRepository<SubComment,String>, JpaSpecificationExecutor<SubComment> {

    SubComment findFirstByCommentIdAndDeleteFlagOrderByPublishTimeAsc(String commentId, Boolean DeleteFlag);

    List<SubComment> findAllByCommentIdAndDeleteFlagOrderByPublishTimeAsc(String commentId, Boolean DeleteFlag);
}
