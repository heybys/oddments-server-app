package com.heybys.oddments.fooddelivery.persistence.user;

import org.springframework.stereotype.Repository;

import com.heybys.oddments.base.jpa.BaseRepository;
import com.heybys.oddments.fooddelivery.domain.user.User;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import com.heybys.oddments.fooddelivery.domain.user.UserRepository;

@Repository
class UserRepositoryImpl extends BaseRepository<User, UserId, UserJpaRepository> implements UserRepository {

    public UserRepositoryImpl(UserJpaRepository repository) {
        super(repository);
    }
}
