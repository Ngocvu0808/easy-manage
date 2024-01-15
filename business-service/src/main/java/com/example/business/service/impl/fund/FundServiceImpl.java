package com.example.business.service.impl.fund;

import com.example.business.config.Constants;
import com.example.business.config.ErrorCodeEnum;
import com.example.business.dto.request.FundRequestData;
import com.example.business.dto.request.RevertRequestData;
import com.example.business.dto.response.FundResponseData;
import com.example.business.entity.Finance;
import com.example.business.entity.FundHistory;
import com.example.business.entity.FundType;
import com.example.business.entity.FungResult;
import com.example.business.repo.FinanceRepository;
import com.example.business.repo.FundRepository;
import com.example.business.service.iface.fund.FundService;
import com.example.business.service.iface.report.ReportService;
import com.example.business.utils.JsonUtils;
import com.example.business.utils.Utils;
import com.example.business.utils.cache.CacheRedisService;
import com.example.business.utils.exception.ResourceNotFoundException;
import java.util.Date;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FundServiceImpl implements FundService {
  private static final Logger logger = org.slf4j.LoggerFactory
      .getLogger(FundServiceImpl.class);
  private final CacheRedisService cacheRedisService;
  private final ReportService reportService;
  private final FinanceRepository financeRepository;
  private final FundRepository fundRepository;

  public FundServiceImpl(CacheRedisService cacheRedisService, ReportService reportService,
      FinanceRepository financeRepository, FundRepository fundRepository) {
    this.cacheRedisService = cacheRedisService;
    this.reportService = reportService;
    this.financeRepository = financeRepository;
    this.fundRepository = fundRepository;
  }

  @Value("${spring.redis.key.balance}")
  private String balanceKey;
  @Value("${product-service.url}")
  private String productServiceUrl;
  @Value("${product-service.auth-key}")
  private String productServiceAuthKey;
  @Value("${product-service.end-point.revert}")
  private String revertEndPoint;

  String getBalanStatus (){
    String balanceFound = cacheRedisService.getValue(balanceKey).toString();
    String[] balanceSplit = balanceFound.split("-");
    String balanceGot = balanceSplit[0];
    return balanceSplit[1];
  }
  @Override
  public FundResponseData fund(FundRequestData data)
      throws ResourceNotFoundException, InterruptedException {
    String balanceFound = cacheRedisService.getValue(balanceKey).toString();
    String[] balanceSplit = balanceFound.split("-");
    String balanceGot = balanceSplit[0];
    String balanceStatus = balanceSplit[1];
    while ("D".equals(balanceStatus)) {
      Thread.sleep(1000);
      balanceStatus = getBalanStatus();
    }
    cacheRedisService.setValue(balanceKey, String.valueOf(balanceGot).concat("-D"));
    if (data.getUserId() == null || data.getUserId() ==0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.ID_BLANK);
    }
    FundResponseData result = new FundResponseData();
    result.setUuid(data.getUuid());
    long balance = Long.parseLong(balanceGot);
    if (FundType.BUYING.name().equals(data.getType()) && balance < data.getAmount()) {
      result.setFundResult(FungResult.FAILED.name());
      HttpResponse<JsonNode> revert = revert(data.getBatch());
      if (revert.getBody() != null) {
        String revertResult = revert.getBody().toString();
        result.setRevert(revertResult);
      }
      cacheRedisService.setValue(balanceKey, String.valueOf(balance).concat("-A"));
      return result;
    }
    balance = balance - data.getAmount();
    cacheRedisService.setValue(balanceKey, String.valueOf(balance).concat("-A"));
    saveData(data, data.getUserId(), balance);
    result.setFundResult(FungResult.SUCCESSED.name());
    return result;
  }

  private void saveData(FundRequestData data, int userId, long balance) {
    Finance finance = new Finance();
    finance.setBalance(balance);
    finance.setUpdateTime(new Date().getTime());
    finance.setTradeType(data.getType());
    finance.setAmount(data.getAmount());
    financeRepository.save(finance);
    FundHistory fundHistory = new FundHistory();
    Utils.myCopyProperties(data, fundHistory);
    fundHistory.setUserId(userId);
    fundRepository.save(fundHistory);
  }
  private HttpResponse<JsonNode> revert(String batch) {
    logger.info("revert() with batch: {}", batch);
    RevertRequestData data = new RevertRequestData();
    data.setBatch(batch);
    HttpResponse<JsonNode> response = Unirest.post(productServiceUrl.concat(revertEndPoint))
        .header("Content-Type", "application/json")
        .header("x-api-key", productServiceAuthKey)
        .header("agent", Constants.AGENT)
        .body(JsonUtils.toJson(data))
        .asJson();
    logger.info("response call api VP: {}", response.getBody());
    return response;
  }
}
