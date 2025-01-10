package com.heybys.oddments.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RSAKeyConfig {

    @Value("${jwt.signing.public-key-location}")
    private Resource signingPublicKeyLocation;

    @Value("${jwt.signing.private-key-location}")
    private Resource signingPrivateKeyLocation;

    @Value("${jwt.encryption.public-key-location}")
    private Resource encryptionPublicKeyLocation;

    @Value("${jwt.encryption.private-key-location}")
    private Resource encryptionPrivateKeyLocation;

    private String readKey(Resource resource) throws IOException {
        // Load the key content from the resource
        return Files.readString(Path.of(resource.getURI()))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                // .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                // .replace("-----END RSA PRIVATE KEY-----", "")
                // .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                // .replace("-----END RSA PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""); // Remove all whitespace
    }

    @Bean
    public RSAPrivateKey signingPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String keyContent = readKey(signingPrivateKeyLocation);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    @Bean
    public RSAPublicKey signingPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String keyContent = readKey(signingPublicKeyLocation);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    @Bean
    public RSAPrivateKey encryptionPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String keyContent = readKey(encryptionPrivateKeyLocation);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    @Bean
    public RSAPublicKey encryptionPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String keyContent = readKey(encryptionPublicKeyLocation);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}
