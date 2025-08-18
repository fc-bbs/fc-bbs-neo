package com.bbs.njtech.user.repo;

import com.bbs.njtech.user.domain.UserAvatarTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAvatarTempRepo extends JpaRepository<UserAvatarTemp,String>, JpaSpecificationExecutor<UserAvatarTemp> {
//    List<AvatarTemp> findAll();
}
