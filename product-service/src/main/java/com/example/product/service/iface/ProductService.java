package com.example.product.service.iface;

import com.example.product.dto.request.AddProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
import com.example.product.dto.response.GetProductResponse;
import com.example.product.entity.Product;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.List;

public interface ProductService {

  int add(AddProductRequest addProductRequest) throws ResourceNotFoundException;

  DataPagingResponse<GetProductResponse> getAll(Integer page, Integer limit, String search,
      String status, String sort, String promotionCode);
  List<GetProductResponse> search(String search);
  DataPagingResponse<GetProductResponse> searchDiscount(String code);

  boolean delete(int id) throws ResourceNotFoundException;

  GetProductResponse findOne(int id) throws ResourceNotFoundException;

  boolean update(int id, UpdateProductRequest request) throws ResourceNotFoundException;

  long getAllValueProduct(long time) throws ParseException;
  List<Long> getListValueProduct(String times ) throws ParseException;


  List<Product> findByIdIn(String ids);
}
