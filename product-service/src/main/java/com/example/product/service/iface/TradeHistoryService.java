package com.example.product.service.iface;

import com.example.product.entity.TradeHistory;
import com.example.product.utils.response.DataPagingResponse;
import java.util.List;

public interface TradeHistoryService {
  DataPagingResponse<TradeHistory> findAll(Integer page, Integer limit, String search,
      String status, String sort);

  List<TradeHistory> findAllByBatch(String batch);
}
