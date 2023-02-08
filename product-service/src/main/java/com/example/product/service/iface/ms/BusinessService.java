package com.example.product.service.iface.ms;

import com.example.product.dto.request.business.FundRequestData;
import com.example.product.utils.exception.ResourceNotFoundException;
import kong.unirest.JsonNode;

public interface BusinessService {

  long checkBalance() throws ResourceNotFoundException;

  JsonNode fund(FundRequestData data);
}
