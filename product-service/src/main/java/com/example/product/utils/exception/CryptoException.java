package com.example.product.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class CryptoException extends BaseException {

  public CryptoException(String message) {
    super(message);
  }

  public CryptoException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}