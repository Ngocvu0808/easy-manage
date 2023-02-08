package com.example.authservice.entities.enums;

/**
 * @author nguyen
 * @created_date 19/09/2020
 */
public enum HttpStatusCustom {
  SUCCESS(200, "Success"), BAD_REQUEST(400, "Bad Request"),
  FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Not Found"),
  CONFLICT(409, "Conflict"), UNAUTHORIZED(401, "Unauthorized"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private final Integer value;
  private final String description;

  HttpStatusCustom(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public Integer getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }
}
