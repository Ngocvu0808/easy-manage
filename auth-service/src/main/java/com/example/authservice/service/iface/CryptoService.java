package com.example.authservice.service.iface;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CryptoService {

  String rsaEncrypt(String data, String base64PublicKey)
      throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException;

  String rsaDecrypt(String data, String base64PrivateKey)
      throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException;
}
