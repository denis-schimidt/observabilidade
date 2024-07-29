package com.schimidt.observability.login;

import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Observed(contextualName = "login-repository")
@Repository
interface LoginRepository extends org.springframework.data.repository.Repository<UserLogin, Long> {

    Optional<UserLogin> findUserByEmail(String email);
}
