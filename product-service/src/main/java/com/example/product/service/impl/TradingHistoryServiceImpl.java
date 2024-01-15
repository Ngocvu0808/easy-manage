package com.example.product.service.impl;

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
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TradingHistoryServiceImpl implements TradeHistoryService {

  private final TradeHistoryRepository tradeHistoryRepository;
  private final ProductRepository productRepository;
  private final SellAddressRepository sellAddressRepository;
  private final String PATTERN = "yyyy-MM-dd";

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
      eachElem.setCreateDate(
          DateUtil.getStringFromDateLongWithPattern(each.getCreateDate(), "dd-MM-yyyy"));
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
  public CustomerTradeHistoryResponse findAllByUserId(int userId) throws ParseException {
    CustomerTradeHistoryResponse result = new CustomerTradeHistoryResponse();
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


  public DataPagingResponse<CusTradeAddrInfo> findAllBillOnline(Integer page, Integer limit,
      String startDate, String endDate) throws ParseException {
    long startTimeLong = DateUtil.getDateFromStringWithPattern(startDate, PATTERN);
    long endTimeLong = DateUtil.getDateFromStringWithPattern(endDate, PATTERN);
    Specification<SellAddressInfo> filter = new SellAddressInfoFilter().filter(startTimeLong, endTimeLong);
    Page<SellAddressInfo> dataPaging = sellAddressRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<SellAddressInfo> sellAddressInfos = dataPaging.getContent();
    List<CusTradeAddrInfo> listContent = new ArrayList<>();
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

}
