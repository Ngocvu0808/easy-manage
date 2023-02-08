package com.example.authservice.utils.response;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class GetMethodResponse<T> {

  private Boolean status;
  private String message;
  private Integer httpCode;
  private T data;
  private String errorCode;

  public GetMethodResponse() {
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

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> GetMethodResponseBuilder<T> builder() {
    return new GetMethodResponseBuilder();
  }

  public Integer getHttpCode() {
    return this.httpCode;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public void setHttpCode(final Integer httpCode) {
    this.httpCode = httpCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof GetMethodResponse)) {
      return false;
    } else {
      GetMethodResponse<?> other = (GetMethodResponse) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label71:
        {
          Object this$status = this.getStatus();
          Object other$status = other.getStatus();
          if (this$status == null) {
            if (other$status == null) {
              break label71;
            }
          } else if (this$status.equals(other$status)) {
            break label71;
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

        label57:
        {
          Object this$httpCode = this.getHttpCode();
          Object other$httpCode = other.getHttpCode();
          if (this$httpCode == null) {
            if (other$httpCode == null) {
              break label57;
            }
          } else if (this$httpCode.equals(other$httpCode)) {
            break label57;
          }

          return false;
        }

        Object this$data = this.getData();
        Object other$data = other.getData();
        if (this$data == null) {
          if (other$data != null) {
            return false;
          }
        } else if (!this$data.equals(other$data)) {
          return false;
        }

        Object this$errorCode = this.getErrorCode();
        Object other$errorCode = other.getErrorCode();
        if (this$errorCode == null) {
          if (other$errorCode == null) {
            return true;
          }
        } else if (this$errorCode.equals(other$errorCode)) {
          return true;
        }

        return false;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof GetMethodResponse;
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
    Object $data = this.getData();
    result = result * 59 + ($data == null ? 43 : $data.hashCode());
    Object $errorCode = this.getErrorCode();
    result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
    return result;
  }

  public String toString() {
    Boolean var10000 = this.getStatus();
    return "GetMethodResponse(status=" + var10000 + ", message=" + this.getMessage() + ", httpCode="
        + this.getHttpCode() + ", data=" + this.getData() + ", errorCode=" + this.getErrorCode()
        + ")";
  }

  public GetMethodResponse(final Boolean status, final String message, final Integer httpCode,
      final T data, final String errorCode) {
    this.status = status;
    this.message = message;
    this.httpCode = httpCode;
    this.data = data;
    this.errorCode = errorCode;
  }

  public static class GetMethodResponseBuilder<T> {

    private Boolean status;
    private String message;
    private Integer httpCode;
    private T data;
    private String errorCode;

    GetMethodResponseBuilder() {
    }

    public GetMethodResponseBuilder<T> status(final Boolean status) {
      this.status = status;
      return this;
    }

    public GetMethodResponseBuilder<T> message(final String message) {
      this.message = message;
      return this;
    }

    public GetMethodResponseBuilder<T> httpCode(final Integer httpCode) {
      this.httpCode = httpCode;
      return this;
    }

    public GetMethodResponseBuilder<T> data(final T data) {
      this.data = data;
      return this;
    }

    public GetMethodResponseBuilder<T> errorCode(final String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public GetMethodResponse<T> build() {
      return new GetMethodResponse(this.status, this.message, this.httpCode, this.data,
          this.errorCode);
    }

    public String toString() {
      return "GetMethodResponse.GetMethodResponseBuilder(status=" + this.status + ", message="
          + this.message + ", httpCode=" + this.httpCode + ", data=" + this.data + ", errorCode="
          + this.errorCode + ")";
    }
  }
}
