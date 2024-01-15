package com.example.customer.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class DuplicateEntityException extends BaseException {

  public DuplicateEntityException(String message) {
    super(message);
  }

  public DuplicateEntityException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
