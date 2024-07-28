package com.schimidt.observability.users;

import com.schimidt.observability.security.PasswordEncryptor;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Timed("user-controller")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
class UserController {

    private final UserService userService;

    private final PasswordEncryptor passwordEncryptor;

    @PostMapping("/users")
    ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) {
        User newUser = userRequest.toEntity(passwordEncryptor);
        User userCreated = userService.create(newUser);

        return ResponseEntity.ok(UserResponse.from(userCreated));
    }

    @GetMapping("/users/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) throws InterruptedException {
        int i = new Random().nextInt(2500);

        Thread.sleep(i);

        return userService.findById(id)
                .map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    List<UserResponse> listAll() {
        return userService.list()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
