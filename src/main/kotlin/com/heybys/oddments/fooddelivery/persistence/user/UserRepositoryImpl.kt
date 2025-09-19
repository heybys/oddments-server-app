package com.heybys.oddments.fooddelivery.persistence.user

import com.heybys.oddments.base.jpa.BaseRepository
import com.heybys.oddments.fooddelivery.domain.user.User
import com.heybys.oddments.fooddelivery.domain.user.UserId
import com.heybys.oddments.fooddelivery.domain.user.UserRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
internal class UserRepositoryImpl(repository: UserJpaRepository) :
    BaseRepository<User, UserId, UserJpaRepository>(repository), UserRepository {

    override fun findByEmail(email: String): Optional<User> {
        return repository.findByEmail(email)
    }
}
