package com.example.product.service.iface;

import com.example.product.dto.request.AddCartRequest;
import com.example.product.dto.response.GetCartByUserIdResponse;

public interface CartService {
  int addCart(AddCartRequest request);
  GetCartByUserIdResponse getCartByUserId(int userId);
}
