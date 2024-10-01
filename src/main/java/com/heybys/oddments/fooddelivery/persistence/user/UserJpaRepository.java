package com.heybys.oddments.fooddelivery.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heybys.oddments.fooddelivery.domain.user.User;
import com.heybys.oddments.fooddelivery.domain.user.UserId;

interface UserJpaRepository extends JpaRepository<User, UserId> {}
