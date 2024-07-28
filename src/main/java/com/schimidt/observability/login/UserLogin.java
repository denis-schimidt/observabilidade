package com.schimidt.observability.login;

import com.schimidt.observability.common.Password;
import com.schimidt.observability.security.PasswordEncryptor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "users")
@Entity
@NoArgsConstructor
@ToString
class UserLogin {

    @Id
    private Long id;

    @Getter
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Password password;

    public String getPassword() {
        return password.getEncryptedPassword();
    }

    public boolean isPasswordValid(String rawPassword, PasswordEncryptor passwordEncryptor) {
        return password.isCorrect(rawPassword, passwordEncryptor);
    }
}