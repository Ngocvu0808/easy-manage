package com.example.product.dto.request.business;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellingOnlineRequest {

  private List<RequestData> products;
  private String customerPhone;
  private Integer userId;
  private String type;

  private Address address;
  @Getter
  @Setter
  public static class Address {
    private String name;
    private String city;
    private String phone;
    private String address;
  }
}
