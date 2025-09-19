package com.heybys.oddments.fooddelivery.domain.user

import com.heybys.oddments.base.domain.Repository
import java.util.Optional

interface UserRepository : Repository<User, UserId> {

    fun findByEmail(email: String): Optional<User>
}
