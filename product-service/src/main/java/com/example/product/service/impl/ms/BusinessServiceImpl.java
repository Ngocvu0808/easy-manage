package com.example.product.service.impl.ms;

import com.example.product.config.Constants;
import com.example.product.config.ErrorCodeEnum;
import com.example.product.dto.request.business.FundRequestData;
import com.example.product.service.iface.ms.BusinessService;
import com.example.product.utils.JsonUtils;
import com.example.product.utils.exception.ResourceNotFoundException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(BusinessServiceImpl.class);

  @Value("${business-service.url}")
  private String businessServiceUrl;
  @Value("${business-service.auth-key}")
  private String businessServiceAuthKey;
  @Value("${business-service.end-point.check-balance}")
  private String businessCheckBalanceEndPoint;
  @Value("${business-service.end-point.fund}")
  private String businessFundEndPoint;
  @Value("${business-service.field.check-balance.balance}")
  private String checkBalanceFieldBalance;

  @Override
  public long checkBalance() throws ResourceNotFoundException {
    logger.info("checkBalance()");
    HttpResponse<JsonNode> response = Unirest.get(
            businessServiceUrl.concat(businessCheckBalanceEndPoint))
        .header("Content-Type", "application/json")
        .header("x-api-key", businessServiceAuthKey)
        .header("agent", Constants.AGENT)
        .asJson();
    logger.info("response call api VP: {}", response.getBody());
    JsonNode jsonNode = null;
    if (response != null) {
      jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from Business-service: {} {}", response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
      }
      if (jsonNode.getObject().has("error")
          && jsonNode.getObject().getInt("error") != 0) {
        throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
      }
    }
    return jsonNode.getObject().optLong(checkBalanceFieldBalance);
  }

  @Override
  public JsonNode fund(String data) {
    logger.info("fund() with data: {}", JsonUtils.toJson(data));
    HttpResponse<JsonNode> response = Unirest.post(businessServiceUrl.concat(businessFundEndPoint))
        .header("Content-Type", "application/json")
        .header("x-api-key", businessServiceAuthKey)
        .header("agent", Constants.AGENT)
        .body(data)
        .asJson();
    logger.info("response call api VP: {}", response.getBody());
    return response.getBody();
  }
}
