package com.heybys.oddments.fooddelivery.controller.user;

import com.heybys.oddments.fooddelivery.domain.user.User;
import com.heybys.oddments.fooddelivery.domain.user.UserId;
import com.heybys.oddments.fooddelivery.domain.user.UserRepository;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public ResponseEntity<Void> addUser(@RequestBody UserAddRequest request) {
        User user = request.toUserDomain();

        userRepository.add(user);

        URI location = URI.create("/api/v1/users/" + user.getId().longValue());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserFindResponse> findUser(@PathVariable Long userId) {
        log.debug("findUser : {}", userId);

        User user = userRepository.find(new UserId(userId));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(UserFindResponse.of(user));
    }
}
