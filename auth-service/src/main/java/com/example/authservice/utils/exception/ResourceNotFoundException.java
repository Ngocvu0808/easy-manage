package com.example.authservice.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
