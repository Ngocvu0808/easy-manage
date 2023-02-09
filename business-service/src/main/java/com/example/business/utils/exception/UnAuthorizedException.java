package com.example.business.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class UnAuthorizedException extends BaseException {

  public UnAuthorizedException(String message) {
    super(message);
  }

  public UnAuthorizedException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
