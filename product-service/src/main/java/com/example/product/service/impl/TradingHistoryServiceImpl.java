package com.example.product.service.impl;

import com.example.product.config.Constants;
import com.example.product.config.ErrorCodeEnum;
import com.example.product.dto.response.trade.CustomerTradeHistoryResponse;
import com.example.product.dto.response.trade.CustomerTradeHistoryResponse.CusTradeAddrInfo;
import com.example.product.dto.response.trade.TradeHistoryResponse;
import com.example.product.entity.Product;
import com.example.product.entity.SellAddressInfo;
import com.example.product.entity.TradeHistory;
import com.example.product.filter.SellAddressInfoFilter;
import com.example.product.filter.TradeFilter;
import com.example.product.repo.ProductRepository;
import com.example.product.repo.SellAddressRepository;
import com.example.product.repo.TradeHistoryRepository;
import com.example.product.service.iface.TradeHistoryService;
import com.example.product.utils.DateUtil;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TradingHistoryServiceImpl implements TradeHistoryService {
  private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(this.getClass());
  private final TradeHistoryRepository tradeHistoryRepository;
  private final ProductRepository productRepository;
  private final SellAddressRepository sellAddressRepository;
  private final String PATTERN = "yyyy-MM-dd";
  @Value("${business-service.url}")
  private String businessServiceUrl;
  @Value("${business-service.auth-key}")
  private String businessServiceAuthKey;
  @Value("${business-service.end-point.find-sell-online}")
  private String businessFindSellOlnEndPoint;
  @Value("${business-service.field.check-balance.balance}")
  private String dataField;
  public TradingHistoryServiceImpl(TradeHistoryRepository tradeHistoryRepository,
      ProductRepository productRepository, SellAddressRepository sellAddressRepository) {
    this.tradeHistoryRepository = tradeHistoryRepository;
    this.productRepository = productRepository;
    this.sellAddressRepository = sellAddressRepository;
  }

  @Override
  public DataPagingResponse<TradeHistoryResponse> findAll(Integer page, Integer limit,
      String search,
      String status, String sort, String startDate, String endDate) throws ParseException {
    long startTimeLong = DateUtil.getDateFromStringWithPattern(startDate, PATTERN);
    long endTimeLong = DateUtil.getDateFromStringWithPattern(endDate, PATTERN);
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Specification<TradeHistory> filter = new TradeFilter().filter(null, search, status, sortMap,
        startTimeLong, endTimeLong);
    Page<TradeHistory> productPages = tradeHistoryRepository.findAll(filter,
        PageRequest.of(page - 1, limit));

    List<TradeHistory> products = productPages.getContent();
    List<TradeHistoryResponse> dataResponse = convertDate(products);
    DataPagingResponse<TradeHistoryResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(dataResponse);
    dataPagingResponses.setTotalPage(productPages.getTotalPages());
    dataPagingResponses.setNum(productPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  List<TradeHistoryResponse> convertDate(List<TradeHistory> products) throws ParseException {
    List<TradeHistoryResponse> result = new ArrayList<>();
    for (TradeHistory each : products) {
      TradeHistoryResponse eachElem = new TradeHistoryResponse();
      BeanUtils.copyProperties(each, eachElem, "createDate");
      List<SellAddressInfo> sellAddressInfos = sellAddressRepository.findAllByBatch(
          each.getBatch());
      if (sellAddressInfos != null && sellAddressInfos.size() == 1) {
        SellAddressInfo sellAddress = sellAddressInfos.get(0);
        eachElem.setCusName(sellAddress.getCusName());
        eachElem.setUsername(sellAddress.getUsername());
      }
      eachElem.setCreateDate(
          DateUtil.getStringFromDateLongWithPattern(each.getCreateDate(), "dd-MM-yyyy HH:mm"));
      result.add(eachElem);
    }
    return result;
  }

  @Override
  public List<TradeHistory> findAllByBatch(String batch) {
    List<TradeHistory> result = tradeHistoryRepository.findAllByBatch(batch);
    return result;
  }

  @Override
  public CustomerTradeHistoryResponse findAllByUserId(int userId)
      throws ParseException, ResourceNotFoundException {
    CustomerTradeHistoryResponse result = new CustomerTradeHistoryResponse();
    JSONObject mapBalance = getListBalance(userId);
    result.setCusId(userId);
    result.setCusTradeInfo(new ArrayList<>());
    List<SellAddressInfo> sellAddressInfos = sellAddressRepository.findAllByCusId(userId);
    if (sellAddressInfos != null && sellAddressInfos.size() > 0) {
      for (SellAddressInfo each : sellAddressInfos) {
        List<TradeHistory> allTrade = tradeHistoryRepository.findAllByBatch(each.getBatch());
        if (allTrade != null && allTrade.size() > 0) {
          CusTradeAddrInfo cusTradeAddrInfo = new CusTradeAddrInfo();
          cusTradeAddrInfo.setName(each.getCusName());
          cusTradeAddrInfo.setAddress(each.getAddress());
          cusTradeAddrInfo.setPhone(each.getPhone());
          cusTradeAddrInfo.setCity(each.getCity());
          cusTradeAddrInfo.setStatus(each.getStatus());
          cusTradeAddrInfo.setBatch(each.getBatch());
          cusTradeAddrInfo.setTotalBill(String.valueOf(mapBalance.getLong(each.getBatch())));
          cusTradeAddrInfo.setDate(
              DateUtil.convertPattern(each.getBatch(), "yyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
          cusTradeAddrInfo.setProducts("");
          for (TradeHistory eachTrade : allTrade) {
            Optional<Product> productOptional = productRepository.findById(eachTrade.getProductId());
            if (productOptional.isPresent() && eachTrade.getAmount() >0) {
              Product p  = productOptional.get();
              cusTradeAddrInfo.setProducts(cusTradeAddrInfo.getProducts().concat(p.getName() + ": " + eachTrade.getAmount() + " "));
            }
          }
          result.getCusTradeInfo().add(cusTradeAddrInfo);
        }
      }
    }

    return result;
  }

  private JSONObject getListBalance(int userId) throws ResourceNotFoundException {
    HttpResponse<JsonNode> response = Unirest.get(
            businessServiceUrl.concat(businessFindSellOlnEndPoint).concat("/" + userId))
        .header("Content-Type", "application/json")
        .header("x-api-key", businessServiceAuthKey)
        .header("agent", Constants.AGENT)
        .asJson();
    logger.info("response call api VP: {}", response);
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
    return jsonNode.getObject().getJSONObject(dataField);
  }
  public DataPagingResponse<CusTradeAddrInfo> findAllBillOnline(Integer page, Integer limit,
      String search, String status) throws ParseException, ResourceNotFoundException {

    Specification<SellAddressInfo> filter = new SellAddressInfoFilter().filter(search, status);
    Page<SellAddressInfo> dataPaging = sellAddressRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<SellAddressInfo> sellAddressInfos = dataPaging.getContent();
    List<CusTradeAddrInfo> listContent = new ArrayList<>();

    if (sellAddressInfos != null && sellAddressInfos.size() > 0) {
      String ids = sellAddressInfos.stream()
          .map(myObject -> myObject.getCusId())
          .distinct() // Loại bỏ các giá trị trùng lặp
          .map(String::valueOf) // Chuyển đổi thành chuỗi
          .collect(Collectors.joining(","));
      JSONObject mapBalance = getListBalanceIn(ids);
      for (SellAddressInfo each : sellAddressInfos) {
        List<TradeHistory> allTrade = tradeHistoryRepository.findAllByBatch(each.getBatch());
        if (allTrade != null && allTrade.size() > 0) {
          CusTradeAddrInfo cusTradeAddrInfo = new CusTradeAddrInfo();
          cusTradeAddrInfo.setName(each.getCusName());
          cusTradeAddrInfo.setAddress(each.getAddress());
          cusTradeAddrInfo.setPhone(each.getPhone());
          cusTradeAddrInfo.setCity(each.getCity());
          cusTradeAddrInfo.setStatus(each.getStatus());
          cusTradeAddrInfo.setBatch(each.getBatch());
          cusTradeAddrInfo.setTotalBill(String.valueOf(mapBalance.getLong(each.getBatch())));
          cusTradeAddrInfo.setDate(
              DateUtil.convertPattern(each.getBatch(), "yyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
          cusTradeAddrInfo.setProducts("");
          for (TradeHistory eachTrade : allTrade) {
            Optional<Product> productOptional = productRepository.findById(eachTrade.getProductId());
            if (productOptional.isPresent() && eachTrade.getAmount() >0) {
              Product p  = productOptional.get();
              cusTradeAddrInfo.setProducts(cusTradeAddrInfo.getProducts().concat(p.getName() + ": " + eachTrade.getAmount() + " "));
            }
          }
          listContent.add(cusTradeAddrInfo);
        }
      }
    }
    DataPagingResponse<CusTradeAddrInfo> result = new DataPagingResponse<>();
    result.setList(listContent);
    result.setTotalPage(dataPaging.getTotalPages());
    result.setNum(dataPaging.getTotalElements());
    result.setCurrentPage(page);
    return result;
  }
  private JSONObject getListBalanceIn(String idList) throws ResourceNotFoundException {
    Map<String, Object> params = new HashMap<>();
    params.put("ids", idList);
    HttpResponse<JsonNode> response = Unirest.get(
            businessServiceUrl.concat(businessFindSellOlnEndPoint))
        .header("Content-Type", "application/json")
        .header("x-api-key", businessServiceAuthKey)
        .header("agent", Constants.AGENT)
        .queryString(params)
        .asJson();
    logger.info("response call api VP: {}", response);
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
      if (jsonNode.getObject().getLong("httpCode") != 200) {
        throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
      }
    }
    return jsonNode.getObject().getJSONObject(dataField);
  }


  public String updateBill(CusTradeAddrInfo requestData) throws ResourceNotFoundException {
    List<SellAddressInfo> sellAddressInfos = sellAddressRepository.findAllByBatch(
        requestData.getBatch());
    if (sellAddressInfos == null || sellAddressInfos.size() != 1) {
      throw new ResourceNotFoundException(ErrorCodeEnum.BILL_NOT_FOUND);
    }
    SellAddressInfo sellAddress = sellAddressInfos.get(0);
    sellAddress.setStatus(requestData.getStatus());
    sellAddress.setUsername(requestData.getUsername());
    sellAddressRepository.save(sellAddress);
    return requestData.getBatch();
  }

}
