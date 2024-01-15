package com.example.business.controller;

import com.example.business.config.Constants;
import com.example.business.config.PermissionObjectCode;
import com.example.business.dto.request.FundRequestData;
import com.example.business.dto.response.FinanceReportResponse;
import com.example.business.dto.response.FundResponseData;
import com.example.business.service.iface.fund.FundService;
import com.example.business.service.iface.report.ReportService;
import com.example.business.utils.auth.AuthGuardService;
import com.example.business.utils.response.BaseMethodResponse;
import com.example.business.utils.response.GetMethodResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/business")
public class BusinessController {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(BusinessController.class);
  private final AuthGuardService authGuard;
  private final FundService fundService;
  private final ReportService reportService;

  public BusinessController(AuthGuardService authGuard, FundService fundService,
      ReportService reportService) {
    this.authGuard = authGuard;
    this.fundService = fundService;
    this.reportService = reportService;
  }


  @PostMapping("/fund")
  public ResponseEntity<?> fund(HttpServletRequest request,
      @RequestBody FundRequestData fundRequestData) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      FundResponseData result = fundService.fund(fundRequestData);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @GetMapping("/check-balance")
  public ResponseEntity<?> getBalance(HttpServletRequest request) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      long result = reportService.getBalance();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @GetMapping("/get-list-balance")
  public ResponseEntity<?> getListBalances(HttpServletRequest request,
      @RequestParam(name = "time") String times) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      List<Long> result = reportService.getListBalance(times);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @GetMapping("/report")
  public ResponseEntity<?> financeReport(HttpServletRequest request,
      @RequestParam(name = "startTime", required = false, defaultValue = "0") Long start,
      @RequestParam(name = "endTime", required = false, defaultValue = "0") Long end) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      List<FinanceReportResponse> result = reportService.getAllValue(start, end);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }
}
