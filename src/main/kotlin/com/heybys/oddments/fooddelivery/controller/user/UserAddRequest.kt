package com.heybys.oddments.fooddelivery.controller.user

import com.heybys.oddments.fooddelivery.domain.generic.AuthProvider
import com.heybys.oddments.fooddelivery.domain.user.User

data class UserAddRequest(
    val username: String,
    val phone: String,
    val email: String,
    val homeCity: String,
    val homeStreet: String,
    val homeZipCode: String,
    val workCity: String,
    val workStreet: String,
    val workZipCode: String,
) {

    fun toUserDomain(): User {
        return User(username, "password", email, "", false, AuthProvider.GOOGLE, "")
    }
}
