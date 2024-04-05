package com.example.business.service.iface.report;

import com.example.business.dto.response.FinanceReportResponse;
import com.example.business.utils.exception.ResourceNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ReportService {
  long getBalance();
  List<Long> getListBalance(String times) throws ParseException;
  List<FinanceReportResponse> getAllValue(long start, long end)
      throws ResourceNotFoundException, ParseException;

  Map<String, Long> getAllFundByUserId(int userId);
  Map<String, Long> getAllFundByUserIdIn(String ids);
}
