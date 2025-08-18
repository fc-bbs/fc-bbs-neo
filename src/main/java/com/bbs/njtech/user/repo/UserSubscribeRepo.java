package com.bbs.njtech.user.repo;

import com.bbs.njtech.user.domain.UserSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserSubscribeRepo extends JpaRepository<UserSubscribe,String>, JpaSpecificationExecutor<UserSubscribe> {
    UserSubscribe findAllByUserIdAndState(String userId,String state);

    UserSubscribe findFirstByUserIdAndUserSubscribeId(String userId,String userSubscribeId);
}
