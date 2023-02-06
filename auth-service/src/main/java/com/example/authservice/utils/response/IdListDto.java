package com.example.authservice.utils.response;

import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class IdListDto <T> {
  private List<T> ids;

  public List<T> getIds() {
    return this.ids;
  }

  public void setIds(final List<T> ids) {
    this.ids = ids;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof IdListDto)) {
      return false;
    } else {
      IdListDto<?> other = (IdListDto)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        Object this$ids = this.getIds();
        Object other$ids = other.getIds();
        if (this$ids == null) {
          if (other$ids != null) {
            return false;
          }
        } else if (!this$ids.equals(other$ids)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof IdListDto;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $ids = this.getIds();
    result = result * 59 + ($ids == null ? 43 : $ids.hashCode());
    return result;
  }

  public String toString() {
    return "IdListDto(ids=" + this.getIds() + ")";
  }

  public IdListDto(final List<T> ids) {
    this.ids = ids;
  }

  public IdListDto() {
  }
}
