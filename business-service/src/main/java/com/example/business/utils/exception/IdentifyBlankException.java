package com.example.business.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class IdentifyBlankException extends BaseException {

  public IdentifyBlankException(String message) {
    super(message);
  }

  public IdentifyBlankException(String message, String messageCode) {
    super(message, messageCode);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
