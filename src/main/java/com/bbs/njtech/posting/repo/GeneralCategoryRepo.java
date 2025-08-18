package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.GeneralCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GeneralCategoryRepo extends JpaRepository<GeneralCategory,String>, JpaSpecificationExecutor<GeneralCategoryRepo> {

        List<GeneralCategory> findByCollegeIdAndDeleteFlagFalse(String CollegeId);

        List<GeneralCategory> findByDeleteFlagFalse();

}
