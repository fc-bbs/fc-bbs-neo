package com.bbs.njtech.user.repo;

import com.bbs.njtech.user.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollegeRepo extends JpaRepository<College,String>, JpaSpecificationExecutor<College> {

    
}
