package com.example.business.service.impl.report;

import com.example.business.config.Constants;
import com.example.business.config.ErrorCodeEnum;
import com.example.business.dto.response.FinanceReportResponse;
import com.example.business.entity.Finance;
import com.example.business.entity.FundHistory;
import com.example.business.repo.FinanceRepository;
import com.example.business.repo.FundRepository;
import com.example.business.service.iface.report.ReportService;
import com.example.business.utils.DateUtil;
import com.example.business.utils.cache.CacheRedisService;
import com.example.business.utils.exception.ResourceNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ReportServiceImpl implements ReportService {

  private static final Logger logger = org.slf4j.LoggerFactory
      .getLogger(ReportServiceImpl.class);
  private final CacheRedisService cacheRedisService;
  private final FinanceRepository financeRepository;
  private final FundRepository fundRepository;
  public ReportServiceImpl(CacheRedisService cacheRedisService, FinanceRepository financeRepository,
      FundRepository fundRepository) {
    this.cacheRedisService = cacheRedisService;
    this.financeRepository = financeRepository;
    this.fundRepository = fundRepository;
  }
  @Value("${spring.redis.key.balance}")
  private String balanceKey;
  @Value("${product-service.url}")
  private String productServiceUrl;
  @Value("${product-service.auth-key}")
  private String productServiceAuthKey;
  @Value("${product-service.end-point.get-all-value}")
  private String allValueEndPoint;
  @Override
  public long getBalance() {
    long result = 0L;
    String balance = cacheRedisService.getValue(balanceKey).toString().split("-")[0];
    if (balance.isBlank()) {
      Finance lastRecord = financeRepository.findTopByOrderByIdDesc();
      if (lastRecord != null) {
        result = lastRecord.getBalance();
      }
    } else {
      result = Long.parseLong(balance);
    }
    return result;
  }

  @Override
  public List<Long> getListBalance(String times) throws ParseException {
    List<Long> result = new ArrayList<>();
    String[] timeSplit = times.split(",");
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    for (String each: timeSplit) {
      if (!StringUtils.isEmpty(each)) {
        long timeFind = df.parse(each).getTime();
        Finance finance = financeRepository.findTopByUpdateTimeIsLessThanOrderByIdDesc(timeFind);
        if (finance == null || finance.getBalance() == 0) {
          result.add(0L);
        } else {
          result.add(finance.getBalance());
        }
      }
    }
    return result;
  }

  @Override
  public List<FinanceReportResponse> getAllValue(long start, long end)
      throws ResourceNotFoundException, ParseException {
    List<FinanceReportResponse> result = new ArrayList<>();
    if (start == 0 && end == 0) {
      FinanceReportResponse each = new FinanceReportResponse();
      Finance finance = financeRepository.findTopByUpdateTimeIsLessThanOrderByIdDesc(DateUtil.getOnlyDateFromTimeStamp(new Date().getTime()));
      if (finance != null) {
        each.setBalance(finance.getBalance());
      }
      each.setTotalProduct( callProductGetAllValue(0));
      result.add(each);
      return result;
    }
    result.add(getEachReport(start));
    result.add(getEachReport(end));
    return result;
  }

  private FinanceReportResponse getEachReport(long time) throws ResourceNotFoundException {
    FinanceReportResponse result = new FinanceReportResponse();
    Finance finance = financeRepository.findTopByUpdateTimeIsLessThanOrderByIdDesc(time);
    if (finance != null) {
      result.setBalance(finance.getBalance());
    }
    result.setTotalProduct(callProductGetAllValue(time));
    return result;
  }

  private long callProductGetAllValue(long time) throws ResourceNotFoundException {
    long result = 0L;
    Map<String, Object> params = new HashMap<>();
    params.put("time", time);
    HttpResponse<JsonNode> response = Unirest.get(
            productServiceUrl.concat(allValueEndPoint))
        .header("Content-Type", "application/json")
        .header("x-api-key", productServiceAuthKey)
        .header("agent", Constants.AGENT)
        .queryString(params)
        .asJson();
    logger.info("response call api VP: {}", response.getBody());
    JsonNode jsonNode = null;
    if (response != null) {
      jsonNode = response.getBody();
      if (response.getStatus() != 200) {
        throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
      }
      if (jsonNode.getObject().has("error")
          && jsonNode.getObject().getInt("error") != 0) {
        throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
      }
      if (jsonNode != null) {
        logger.info("response from Business-service: {} {}", response.getStatus(),
            jsonNode.toString());
      }
      result = jsonNode.getObject().getLong("data");
    }
    return result;
  }

  public Map<String, Long> getAllFundByUserId(int userId) {
    List<FundHistory> fundHisList = fundRepository.findAllByUserId(userId);
    Map<String, Long> result = new HashMap<>();
    if (fundHisList == null)
      return result;
    for (FundHistory each : fundHisList) {
      result.put(each.getBatch(), each.getAmount());
    }
    return result;
  }


  public Map<String, Long> getAllFundByUserIdIn(String ids) {
    String[] idSplit = ids.split(",");
    List<Integer> idList = Arrays.stream(idSplit)
        .map(Integer::parseInt) // Chuyển đổi từ chuỗi sang Integer
        .collect(Collectors.toList());
    List<FundHistory> fundHisList = fundRepository.findAllByUserIdIn(idList);
    Map<String, Long> result = new HashMap<>();
    if (fundHisList == null)
      return result;
    for (FundHistory each : fundHisList) {
      result.put(each.getBatch(), each.getAmount());
    }
    return result;
  }
}
