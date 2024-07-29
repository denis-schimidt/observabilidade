package com.schimidt.observability.login;

import com.schimidt.observability.security.PasswordEncryptor;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Observed(contextualName = "login-service")
@RequiredArgsConstructor
@Service
class LoginService {

    private final PasswordEncryptor passwordEncryptor;
    private final LoginRepository loginRepository;
    private final LoginMetrics metrics;

    LoginResult login(UserLoginRequest userLoginRequest) {

        var loginResult = loginRepository.findUserByEmail(userLoginRequest.email())
                .filter(user -> user.isPasswordValid(userLoginRequest.rawPassword(), passwordEncryptor))
                .map(_ -> LoginResult.LOGGED)
                .orElse(LoginResult.NOT_LOGGED);

        metrics.countLogin(loginResult, userLoginRequest.email());

        return loginResult;
    }
}
