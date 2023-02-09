package com.example.business.service.iface.report;

import com.example.business.dto.response.FinanceReportResponse;
import com.example.business.utils.exception.ResourceNotFoundException;
import java.text.ParseException;
import java.util.List;

public interface ReportService {
  long getBalance();
  List<FinanceReportResponse> getAllValue(long start, long end)
      throws ResourceNotFoundException, ParseException;
}
