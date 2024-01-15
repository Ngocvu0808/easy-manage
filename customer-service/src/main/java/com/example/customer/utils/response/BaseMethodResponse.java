package com.example.customer.utils.response;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class BaseMethodResponse {

  private Boolean status;
  private String message;
  private Integer httpCode;
  private String errorCode;

  public BaseMethodResponse() {
  }

  public Boolean getStatus() {
    return this.status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getHttpCode() {
    return this.httpCode;
  }

  public void setHttpCode(Integer httpCode) {
    this.httpCode = httpCode;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public static BaseMethodResponseBuilder builder() {
    return new BaseMethodResponseBuilder();
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof BaseMethodResponse)) {
      return false;
    } else {
      BaseMethodResponse other = (BaseMethodResponse) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label59:
        {
          Object this$status = this.getStatus();
          Object other$status = other.getStatus();
          if (this$status == null) {
            if (other$status == null) {
              break label59;
            }
          } else if (this$status.equals(other$status)) {
            break label59;
          }

          return false;
        }

        Object this$message = this.getMessage();
        Object other$message = other.getMessage();
        if (this$message == null) {
          if (other$message != null) {
            return false;
          }
        } else if (!this$message.equals(other$message)) {
          return false;
        }

        Object this$httpCode = this.getHttpCode();
        Object other$httpCode = other.getHttpCode();
        if (this$httpCode == null) {
          if (other$httpCode != null) {
            return false;
          }
        } else if (!this$httpCode.equals(other$httpCode)) {
          return false;
        }

        Object this$errorCode = this.getErrorCode();
        Object other$errorCode = other.getErrorCode();
        if (this$errorCode == null) {
          if (other$errorCode != null) {
            return false;
          }
        } else if (!this$errorCode.equals(other$errorCode)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof BaseMethodResponse;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $status = this.getStatus();
    result = result * 59 + ($status == null ? 43 : $status.hashCode());
    Object $message = this.getMessage();
    result = result * 59 + ($message == null ? 43 : $message.hashCode());
    Object $httpCode = this.getHttpCode();
    result = result * 59 + ($httpCode == null ? 43 : $httpCode.hashCode());
    Object $errorCode = this.getErrorCode();
    result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
    return result;
  }

  public String toString() {
    Boolean var10000 = this.getStatus();
    return "BaseMethodResponse(status=" + var10000 + ", message=" + this.getMessage() +
        ", httpCode=" + this.getHttpCode() + ", errorCode=" + this.getErrorCode() + ")";
  }

  public BaseMethodResponse(final Boolean status, final String message, final Integer httpCode,
      final String errorCode) {
    this.status = status;
    this.message = message;
    this.httpCode = httpCode;
    this.errorCode = errorCode;
  }

  public static class BaseMethodResponseBuilder {

    private Boolean status;
    private String message;
    private Integer httpCode;
    private String errorCode;

    BaseMethodResponseBuilder() {
    }

    public BaseMethodResponseBuilder status(final Boolean status) {
      this.status = status;
      return this;
    }

    public BaseMethodResponseBuilder message(final String message) {
      this.message = message;
      return this;
    }

    public BaseMethodResponseBuilder httpCode(final Integer httpCode) {
      this.httpCode = httpCode;
      return this;
    }

    public BaseMethodResponseBuilder errorCode(final String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public BaseMethodResponse build() {
      return new BaseMethodResponse(this.status, this.message, this.httpCode, this.errorCode);
    }

    public String toString() {
      return "BaseMethodResponse.BaseMethodResponseBuilder(status=" + this.status + ", message=" +
          this.message + ", httpCode=" + this.httpCode + ", errorCode=" + this.errorCode + ")";
    }
  }
}
