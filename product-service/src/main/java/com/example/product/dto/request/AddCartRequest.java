package com.example.product.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartRequest {
  private int userId;
  private List<CartProduct> products;
  @Getter
  @Setter
  public static class CartProduct {
    private int productId;
    private int amount;
  }
}
