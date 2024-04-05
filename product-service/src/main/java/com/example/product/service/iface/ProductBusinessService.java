package com.example.product.service.iface;

import com.example.product.dto.request.business.BuyingProductRequest;
import com.example.product.dto.request.business.SellingOnlineRequest;
import com.example.product.dto.request.business.SellingProductRequest;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.exception.UnAuthorizedException;
import javax.servlet.http.HttpServletRequest;

public interface ProductBusinessService {

  boolean buy(BuyingProductRequest request) throws ResourceNotFoundException;

  boolean sell(HttpServletRequest servletRequest, SellingProductRequest request)
      throws ResourceNotFoundException, UnAuthorizedException;
  boolean sellOnline(SellingOnlineRequest request) throws ResourceNotFoundException;
  boolean revert(String batch) throws ResourceNotFoundException;
}
