package com.bbs.njtech.card.repo;

import com.bbs.njtech.card.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityRepo extends JpaRepository<Activity,String>, JpaSpecificationExecutor<Activity> {

}
