package com.heybys.oddments.persistence.user;

import com.heybys.oddments.domain.user.User;
import com.heybys.oddments.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, UserId> {}
