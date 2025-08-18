package com.bbs.njtech.user.repo;

import com.bbs.njtech.user.domain.UserFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserFavoritesRepo extends JpaRepository<UserFavorites,String>, JpaSpecificationExecutor<UserFavorites> {

    Integer countAllByPostMessageIdAndState(String postMessageId,String state);

    UserFavorites findFirstByUserIdAndPostMessageId(String userId,String postMessageId);
}
