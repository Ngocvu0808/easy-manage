package com.example.authservice.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class ProxyAuthenticationException extends BaseException {
  public ProxyAuthenticationException(String message) {
    super(message);
  }

  public ProxyAuthenticationException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}

