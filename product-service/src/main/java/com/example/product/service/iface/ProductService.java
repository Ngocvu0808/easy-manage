package com.example.product.service.iface;

import com.example.product.dto.request.AddProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
import com.example.product.dto.response.GetProductResponse;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;

public interface ProductService {

  int add(AddProductRequest addProductRequest);

  DataPagingResponse<GetProductResponse> getAll(Integer page, Integer limit, String search,
      String status, String sort);

  boolean delete(int id) throws ResourceNotFoundException;

  GetProductResponse findOne(int id) throws ResourceNotFoundException;

  boolean update(int id, UpdateProductRequest request) throws ResourceNotFoundException;

  long getAllValueProduct(long time) throws ParseException;

}
