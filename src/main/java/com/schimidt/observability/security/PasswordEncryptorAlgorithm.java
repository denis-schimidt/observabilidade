package com.schimidt.observability.security;

import com.schimidt.observability.users.CryptographyException;
import com.schimidt.observability.users.EncryptionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class PasswordEncryptorAlgorithm implements PasswordEncryptor {

    private final EncryptionProperties encryptionProperties;

    @Override
    public String encrypt(String rawPassword, String saltEncoded) throws CryptographyException {

        try {
            String saltAlreadyUsedOrNew = ofNullable(saltEncoded).orElseGet(this::getSaltEncoded);

            byte[] salt = Base64.getDecoder().decode(saltAlreadyUsedOrNew);
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, encryptionProperties.iterations(), encryptionProperties.keyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(encryptionProperties.algorithm());

            byte[] passwordEncoded = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(passwordEncoded);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptographyException(e.getMessage());
        }
    }

    @Override
    public String getSaltEncoded() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }
}
