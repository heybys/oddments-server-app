package com.heybys.oddments.controller;

import com.heybys.oddments.domain.user.User;
import com.heybys.oddments.domain.user.UserId;
import com.heybys.oddments.domain.user.UserRepository;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
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

        User user = userRepository.find(new UserId(userId));

        return ResponseEntity.ok(UserFindResponse.of(user));
    }
}
