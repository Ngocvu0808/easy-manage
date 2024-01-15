package com.example.product.service.iface;

import com.example.product.dto.request.business.BuyingProductRequest;
import com.example.product.dto.request.business.SellingOnlineRequest;
import com.example.product.dto.request.business.SellingProductRequest;
import com.example.product.utils.exception.ResourceNotFoundException;

public interface ProductBusinessService {

  boolean buy(BuyingProductRequest request) throws ResourceNotFoundException;

  boolean sell(SellingProductRequest request) throws ResourceNotFoundException;
  boolean sellOnline(SellingOnlineRequest request) throws ResourceNotFoundException;
  boolean revert(String batch) throws ResourceNotFoundException;
}
