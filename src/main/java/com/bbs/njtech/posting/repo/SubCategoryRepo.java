package com.bbs.njtech.posting.repo;

import com.bbs.njtech.posting.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SubCategoryRepo extends JpaRepository<SubCategory,String> , JpaSpecificationExecutor<SubCategory> {

    List<SubCategory> findByDeleteFlagFalse();

    SubCategory findByNameAndDeleteFlagFalse(String name);

}
