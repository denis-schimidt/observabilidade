package com.schimidt.observability.login;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
class LoginController {

    private final LoginService loginService;

    @PostMapping("/logins")
    @Timed("login")
    ResponseEntity<LoginResult> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        LoginResult login = loginService.login(userLoginRequest);

        if (login == LoginResult.LOGGED) {
            log.info("User {} logged in", userLoginRequest.email());
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(LoginResult.LOGGED);
        }

        log.warn("Failed login attempt for user {}", userLoginRequest.email());

        return ResponseEntity.status(UNAUTHORIZED).build();
    }
}
