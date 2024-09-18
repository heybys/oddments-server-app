package com.heybys.oddments.fooddelivery.persistence.user;

import com.heybys.oddments.fooddelivery.domain.user.User;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, UserId> {}
