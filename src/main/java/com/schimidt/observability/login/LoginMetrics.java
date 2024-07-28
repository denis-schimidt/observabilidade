package com.schimidt.observability.login;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginMetrics {

    private final MeterRegistry registry;

    public void countLogin(LoginResult loginResult, String email) {
        Counter.builder("Login")
                .description("counter of logins")
                .tag("email", email)
                .tag("result", loginResult.name())
                .register(registry)
                .increment(1);
    }
}

