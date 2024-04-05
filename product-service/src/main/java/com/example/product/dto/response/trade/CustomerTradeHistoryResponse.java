package com.example.product.dto.response.trade;

import com.example.product.dto.request.business.RequestData;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerTradeHistoryResponse {
  private int cusId;
  private List<CusTradeAddrInfo> cusTradeInfo;

  @Getter
  @Setter
  public static class CusTradeAddrInfo {
    private int sellId;
    private String name;
    private String city;
    private String phone;
    private String address;
    private String date;
    private String totalBill;
    private String products;
    private String status;
    private String batch;
    private String username;
  }
}
