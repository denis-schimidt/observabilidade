package com.schimidt.observability.users;

import com.schimidt.observability.common.Password;
import com.schimidt.observability.security.PasswordEncryptor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

record UserRequest(@NotBlank String name, @Email String email, @NotBlank String rawPassword, @Valid @NotNull Set<AddressRequest> addresses) {

    User toEntity(PasswordEncryptor passwordEncryptor) {
        return User.of(name, email, Password.newPassword(rawPassword, passwordEncryptor),
                addresses.stream().map(AddressRequest::toEntity).collect(Collectors.toSet()));
    }
}
