package com.heybys.oddments.fooddelivery.domain.user;

import java.util.Optional;

import com.heybys.oddments.base.domain.Repository;

public interface UserRepository extends Repository<User, UserId> {

    Optional<User> findByEmail(String email);
}
