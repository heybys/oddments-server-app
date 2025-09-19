package com.heybys.oddments.fooddelivery.persistence.user

import com.heybys.oddments.fooddelivery.domain.user.User
import com.heybys.oddments.fooddelivery.domain.user.UserId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

internal interface UserJpaRepository : JpaRepository<User, UserId> {
    fun findByEmail(email: String): Optional<User>
}
