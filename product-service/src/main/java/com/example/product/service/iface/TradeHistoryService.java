package com.example.product.service.iface;

import com.example.product.dto.response.trade.CustomerTradeHistoryResponse;
import com.example.product.dto.response.trade.CustomerTradeHistoryResponse.CusTradeAddrInfo;
import com.example.product.dto.response.trade.TradeHistoryResponse;
import com.example.product.entity.TradeHistory;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.List;

public interface TradeHistoryService {
  DataPagingResponse<TradeHistoryResponse> findAll(Integer page, Integer limit, String search,
      String status, String sort, String startDate, String endDate) throws ParseException;

  List<TradeHistory> findAllByBatch(String batch);
  CustomerTradeHistoryResponse findAllByUserId(int userId)
      throws ParseException, ResourceNotFoundException;
  DataPagingResponse<CusTradeAddrInfo> findAllBillOnline(Integer page, Integer limit,
      String search, String status) throws ParseException, ResourceNotFoundException;
  String updateBill(CusTradeAddrInfo requestData) throws ResourceNotFoundException;
}
