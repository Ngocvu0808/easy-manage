package com.example.business.utils.exception;

import com.example.business.config.ErrorCodeEnum;

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

  public ResourceNotFoundException(ErrorCodeEnum error) {
    super(error);
  }

  public String getMessage() {
    return super.getMessage();
  }
}
