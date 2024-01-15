package com.example.business.service.iface.fund;

import com.example.business.dto.request.FundRequestData;
import com.example.business.dto.response.FundResponseData;
import com.example.business.utils.exception.ResourceNotFoundException;

public interface FundService {
  FundResponseData fund(FundRequestData data) throws ResourceNotFoundException, InterruptedException;
}
