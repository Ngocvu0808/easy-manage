package com.example.authservice.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class UserNotFoundException extends BaseException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
