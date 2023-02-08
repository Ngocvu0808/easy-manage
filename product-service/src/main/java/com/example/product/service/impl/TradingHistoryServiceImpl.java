package com.example.product.service.impl;

import com.example.product.entity.TradeHistory;
import com.example.product.filter.TradeFilter;
import com.example.product.repo.TradeHistoryRepository;
import com.example.product.service.iface.TradeHistoryService;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.response.DataPagingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TradingHistoryServiceImpl implements TradeHistoryService {
  private final TradeHistoryRepository tradeHistoryRepository;

  public TradingHistoryServiceImpl(TradeHistoryRepository tradeHistoryRepository) {
    this.tradeHistoryRepository = tradeHistoryRepository;
  }

  @Override
  public DataPagingResponse<TradeHistory> findAll(Integer page, Integer limit, String search,
      String status, String sort) {
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Specification<TradeHistory> filter = new TradeFilter().filter(null, search, status, sortMap, false);
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
