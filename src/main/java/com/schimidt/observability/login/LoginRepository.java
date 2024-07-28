package com.schimidt.observability.login;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface LoginRepository extends org.springframework.data.repository.Repository<UserLogin, Long> {

    Optional<UserLogin> findUserByEmail(String email);
}
