package com.bbs.njtech.user.repo;

import com.bbs.njtech.user.domain.UserNickNameTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserNickNameTempRepo extends JpaRepository<UserNickNameTemp,String>, JpaSpecificationExecutor<UserNickNameTemp> {

}
