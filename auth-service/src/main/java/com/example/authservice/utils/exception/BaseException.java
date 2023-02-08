package com.example.authservice.utils.exception;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class BaseException extends Exception {

  protected String messageCode;

  public BaseException(String message) {
    super(message);
  }

  public BaseException(String message, String messageCode) {
    super("#" + messageCode + " " + message);
    this.messageCode = messageCode;
  }

  public String getMessageCode() {
    return this.messageCode;
  }

  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }
}
