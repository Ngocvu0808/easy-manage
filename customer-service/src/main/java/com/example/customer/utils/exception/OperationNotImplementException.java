package com.example.customer.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class OperationNotImplementException extends BaseException {

  public OperationNotImplementException(String message) {
    super(message);
  }

  public OperationNotImplementException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
