package com.bbs.njtech.card.repo;

import com.bbs.njtech.card.domain.UserVipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserVipCardRepo extends JpaRepository<UserVipCard,String>, JpaSpecificationExecutor<UserVipCard> {
    UserVipCard findUserVipCardByUserId(String userId);

}
