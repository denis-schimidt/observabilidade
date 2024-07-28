package com.schimidt.observability.common;

import com.schimidt.observability.security.PasswordEncryptor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class Password {

    @Column(nullable = false, name = "encrypted_password", length = 60)
    @NotBlank
    private String encryptedPassword;

    @Column(nullable = false, name = "encrypted_salt", length = 60)
    @NotBlank
    private String encryptedSalt;

    /**
     * @deprecated Hibernate eyes only
     */
    Password() {
    }

    private Password(String rawPassword, PasswordEncryptor passwordEncryptor, String saltEncoded) {
        this.encryptedPassword = passwordEncryptor.encrypt(rawPassword, saltEncoded);
        this.encryptedSalt = saltEncoded;
    }

    private static Password alreadySaved(String rawPassword, PasswordEncryptor passwordEncryptor, String saltEncoded) {
        return new Password(rawPassword, passwordEncryptor, saltEncoded);
    }

    public static Password newPassword(String rawPassword, PasswordEncryptor passwordEncryptor) {
        return new Password(rawPassword, passwordEncryptor, passwordEncryptor.getSaltEncoded());
    }

    public boolean isCorrect(String rawPassword, PasswordEncryptor passwordEncryptor) {
        return this.equals(Password.alreadySaved(rawPassword, passwordEncryptor, encryptedSalt));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;
        return Objects.equals(encryptedPassword, password.encryptedPassword) && Objects.equals(encryptedSalt, password.encryptedSalt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(encryptedPassword);
        result = 31 * result + Objects.hashCode(encryptedSalt);
        return result;
    }
}
