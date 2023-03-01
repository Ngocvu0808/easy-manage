package com.example.product.service.impl;

import com.example.product.dto.response.trade.TradeHistoryResponse;
import com.example.product.entity.Product;
import com.example.product.entity.TradeHistory;
import com.example.product.filter.TradeFilter;
import com.example.product.repo.ProductRepository;
import com.example.product.repo.TradeHistoryRepository;
import com.example.product.service.iface.TradeHistoryService;
import com.example.product.utils.DateUtil;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TradingHistoryServiceImpl implements TradeHistoryService {
  private final TradeHistoryRepository tradeHistoryRepository;
  private final ProductRepository productRepository;
private final String PATTERN = "yyyy-MM-dd";
  public TradingHistoryServiceImpl(TradeHistoryRepository tradeHistoryRepository,
      ProductRepository productRepository) {
    this.tradeHistoryRepository = tradeHistoryRepository;
    this.productRepository = productRepository;
  }

  @Override
  public DataPagingResponse<TradeHistory> findAll(Integer page, Integer limit, String search,
      String status, String sort, String startDate, String endDate) throws ParseException {
    long startTimeLong = DateUtil.getDateFromStringWithPattern(startDate, PATTERN);
    long endTimeLong = DateUtil.getDateFromStringWithPattern(endDate, PATTERN);
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Specification<TradeHistory> filter = new TradeFilter().filter(null, search, status, sortMap,
         startTimeLong, endTimeLong);
    Page<TradeHistory> productPages = tradeHistoryRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<TradeHistory> products = productPages.getContent();

    DataPagingResponse<TradeHistory> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(products);
    dataPagingResponses.setTotalPage(productPages.getTotalPages());
    dataPagingResponses.setNum(productPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }


  @Override
  public List<TradeHistory> findAllByBatch(String batch) {
    List<TradeHistory> result = tradeHistoryRepository.findAllByBatch(batch);
    return result;
  }
}
