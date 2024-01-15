package com.example.product.dto.response;

import com.example.product.entity.Product;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCartByUserIdResponse {
  private int id;
  private int userId;
  private List<ProductInCart> products;


  @Getter
  @Setter
  public static class ProductInCart {
    private int amount;
    private Product product;
  }
}
