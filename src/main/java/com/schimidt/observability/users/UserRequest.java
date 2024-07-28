package com.schimidt.observability.users;

import com.schimidt.observability.common.Password;
import com.schimidt.observability.security.PasswordEncryptor;
import jakarta.validation.constraints.Email;

record UserRequest(String name, @Email String email, String rawPassword) {

    User toEntity(PasswordEncryptor passwordEncryptor) {
        return User.of(name, email, Password.newPassword(rawPassword, passwordEncryptor));
    }
}
