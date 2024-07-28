package com.schimidt.observability.security;

import com.schimidt.observability.users.CryptographyException;

public interface PasswordEncryptor {

    String getSaltEncoded();

    String encrypt(String rawPassword, String saltEncoded) throws CryptographyException;
}
