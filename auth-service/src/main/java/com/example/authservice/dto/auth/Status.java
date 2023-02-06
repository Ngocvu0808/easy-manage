package com.example.authservice.dto.auth;

public enum Status {
  ACTIVE(0),
  CONFIRMED(1),
  APPROVED(2),
  DELETED(3),
  DUPLICATE(4),
  GRACE_PERIOD(5),
  INVITED(6),
  DENIED(7),
  PENDING(8),
  PENDING_APPROVAL(9),
  PENDING_CONFIRMATION(10),
  SUSPENDED(11),
  DECLINED(12),
  EXPIRED(13);
  private final int value;

  private Status(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Status findByValue(int value) {
    switch (value) {
      case 0:
        return ACTIVE;
      case 1:
        return CONFIRMED;
      case 2:
        return APPROVED;
      case 3:
        return DELETED;
      case 4:
        return DUPLICATE;
      case 5:
        return GRACE_PERIOD;
      case 6:
        return INVITED;
      case 7:
        return DENIED;
      case 8:
        return PENDING;
      case 9:
        return PENDING_APPROVAL;
      case 10:
        return PENDING_CONFIRMATION;
      case 11:
        return SUSPENDED;
      case 12:
        return DECLINED;
      case 13:
        return EXPIRED;
      default:
        return null;
    }
  }
}
