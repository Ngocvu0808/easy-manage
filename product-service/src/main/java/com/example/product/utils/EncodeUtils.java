package com.example.product.utils;

import java.util.Base64;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class EncodeUtils {

  public EncodeUtils() {
  }

  public static String decodeJWT(String jwt) {
    String[] split_string = jwt.split("\\.");
    if (split_string.length != 3) {
      return null;
    } else {
      String base64EncodedBody = split_string[1];
      return new String(Base64.getDecoder().decode(base64EncodedBody));
    }
  }

  public static String appendJWT(String jwt, String newBody) {
    String[] split_string = jwt.split("\\.");
    split_string[1] = Base64.getUrlEncoder().withoutPadding().encodeToString(newBody.getBytes());
    return String.join(".", split_string);
  }

  public static String bytesToHex(byte[] hashInBytes) {
    StringBuilder sb = new StringBuilder();
    byte[] var2 = hashInBytes;
    int var3 = hashInBytes.length;

    for (int var4 = 0; var4 < var3; ++var4) {
      byte b = var2[var4];
      sb.append(String.format("%02x", b));
    }

    return sb.toString();
  }
}
