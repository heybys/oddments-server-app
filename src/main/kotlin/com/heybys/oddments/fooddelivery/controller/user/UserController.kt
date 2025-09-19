package com.heybys.oddments.fooddelivery.controller.user

import com.heybys.oddments.fooddelivery.domain.user.UserId
import com.heybys.oddments.fooddelivery.domain.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userRepository: UserRepository) {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("")
    fun addUser(@RequestBody request: UserAddRequest): ResponseEntity<Void> {
        val user = request.toUserDomain()

        userRepository.save(user)

        val location = URI.create("/api/v1/user/" + user.getId()!!.longValue())

        return ResponseEntity.created(location).build()
    }

    @GetMapping("/{userId}")
    fun findUser(@PathVariable userId: Long): ResponseEntity<UserFindResponse> {
        log.debug("findUser : {}", userId)

        val user = userRepository.find(UserId(userId))
        if (user == null) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(UserFindResponse.of(user))
    }
}
