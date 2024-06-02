package com.sap.notebook.utils;

import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Date;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;


@Component
public class JwtUtils {

    private final ResourceLoader resourceLoader;

    public JwtUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        Security.addProvider(new BouncyCastleProvider());
    }

    public String generateJWT(String appId, String privateKeyPath) throws IOException {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey)privateKey);

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (10 * 60 * 1000); // 10 minutes

        return JWT.create()
                .withIssuer(appId)
                .withIssuedAt(new Date(nowMillis))
                .withExpiresAt(new Date(expMillis))
                .sign(algorithm);
    }

    private PrivateKey getPrivateKey(String privateKeyPath) {
        Resource resource = resourceLoader.getResource(privateKeyPath);
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof PEMKeyPair) {
                // PKCS#1 formatted private key
                PEMKeyPair pemKeyPair = (PEMKeyPair) object;
                PrivateKeyInfo privateKeyInfo = pemKeyPair.getPrivateKeyInfo();
                return converter.getPrivateKey(privateKeyInfo);
            } else if (object instanceof PrivateKeyInfo) {
                // PKCS#8 formatted private key
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
                return converter.getPrivateKey(privateKeyInfo);
            } else if (object instanceof RSAPrivateKey) {
                // PKCS#1 formatted RSA private key
                RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) object;
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePrivate(keySpec);
            } else {
                throw new IllegalArgumentException("Invalid PEM file: unknown format");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }

}
