package com.heybys.oddments.fooddelivery.controller.user

import com.heybys.oddments.fooddelivery.domain.generic.Address
import com.heybys.oddments.fooddelivery.domain.generic.Contact
import com.heybys.oddments.fooddelivery.domain.user.User

data class UserFindResponse(
    val username: String,
    val contact: Contact?,
    val homeAddress: Address?,
    val workAddress: Address?,
) {

    companion object {
        @JvmStatic
        fun of(user: User): UserFindResponse {
            return UserFindResponse(user.name!!, null, null, null)
        }
    }
}
