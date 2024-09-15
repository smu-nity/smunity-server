package com.smunity.server.global.security.config.encoder;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Pbkdf2PasswordEncoder implements PasswordEncoder {

    private final StringKeyGenerator saltGenerator = KeyGenerators.string();
    private final String PREFIX = "pbkdf2_sha256";
    private final int ITERATIONS = 390000;

    @Override
    public String encode(CharSequence rawPassword) {
        String salt = saltGenerator.generateKey();
        byte[] hash = encodeWithSalt(rawPassword, salt.getBytes(StandardCharsets.US_ASCII));
        String encodedHash = base64Encode(hash);
        return String.join("$", Arrays.asList(PREFIX, Integer.toString(ITERATIONS), salt, encodedHash));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (!encodedPassword.startsWith(PREFIX))
            throw new IllegalArgumentException("Encoded password does not start with: $PREFIX");
        String[] parts = encodedPassword.split("\\$");
        if (parts.length != 4)
            throw new IllegalArgumentException("The encoded password format does not have 4 parts");
        int iterations = Integer.parseInt(parts[1]);
        byte[] salt = parts[2].getBytes(StandardCharsets.US_ASCII);
        byte[] hash = base64Decode(parts[3]);
        return MessageDigest.isEqual(hash, encodeWithSaltAndIterations(rawPassword, salt, iterations));
    }

    private byte[] base64Decode(String string) {
        return Base64.getDecoder().decode(string);
    }

    private String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] encodeWithSaltAndIterations(CharSequence rawPassword, byte[] salt, int iterations) {
        PBEKeySpec keySpec = new PBEKeySpec(rawPassword.toString().toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not create hash", e);
        }
    }

    private byte[] encodeWithSalt(CharSequence rawPassword, byte[] salt) {
        return encodeWithSaltAndIterations(rawPassword, salt, ITERATIONS);
    }
}
