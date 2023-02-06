package com.example.authservice.service.impl;


import com.example.authservice.service.iface.CryptoService;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {
    public CryptoServiceImpl() {
    }

    public String rsaEncrypt(String data, String base64PublicKey) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException {
        return this.encrypt(data, base64PublicKey);
    }

    public String rsaDecrypt(String data, String base64PrivateKey) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        return this.decrypt(data, base64PrivateKey);
    }

    private PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private PrivateKey getPrivateKey(String base64PrivateKey) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            try {
                return keyFactory.generatePrivate(keySpec);
            } catch (InvalidKeySpecException var5) {
                var5.printStackTrace();
            }
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
        }

        return null;
    }

    public String encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, this.getPublicKey(publicKey));
        return Arrays.toString(cipher.doFinal(data.getBytes()));
    }

    public String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return this.decrypt(Base64.getDecoder().decode(data.getBytes()), this.getPrivateKey(base64PrivateKey));
    }

    public String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, privateKey);
        return new String(cipher.doFinal(data));
    }
}
