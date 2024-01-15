package com.example.product.controller;

import com.example.product.config.Constants;
import com.example.product.config.MessageCode;
import com.example.product.config.PermissionObjectCode;
import com.example.product.config.PermissionObjectCode.ProductPermissionCode;
import com.example.product.config.PermissionObjectCode.RoleCode;
import com.example.product.dto.request.business.BuyingProductRequest;
import com.example.product.dto.request.business.RevertRequestData;
import com.example.product.dto.request.business.SellingOnlineRequest;
import com.example.product.dto.request.business.SellingProductRequest;
import com.example.product.dto.response.GetProductResponse;
import com.example.product.dto.response.trade.CustomerTradeHistoryResponse;
import com.example.product.dto.response.trade.CustomerTradeHistoryResponse.CusTradeAddrInfo;
import com.example.product.dto.response.trade.TradeHistoryResponse;
import com.example.product.entity.TradeHistory;
import com.example.product.service.iface.ProductBusinessService;
import com.example.product.service.iface.TradeHistoryService;
import com.example.product.utils.ServiceInfo;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.auth.AuthGuardService;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.BaseMethodResponse;
import com.example.product.utils.response.DataPagingResponse;
import com.example.product.utils.response.GetMethodResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/trading")
public class TradingController {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(TradingController.class);
  private final AuthGuardService authGuard;
  private final TradeHistoryService tradeHistoryService;
  private final ProductBusinessService businessService;
  public TradingController(AuthGuardService authGuard, TradeHistoryService tradeHistoryService,
      ProductBusinessService businessService) {
    this.authGuard = authGuard;
    this.tradeHistoryService = tradeHistoryService;
    this.businessService = businessService;
  }

  @GetMapping("")
  public ResponseEntity<?> getAll(HttpServletRequest request,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "startDate", required = false) String startDate,
      @RequestParam(name = "endDate", required = false) String endDate,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.ProductPermissionCode.PRODUCT_TRADING_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, GetProductResponse.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(ServiceInfo.getId() + MessageCode.SORT_INVALID)
                .httpCode(HttpStatus.BAD_REQUEST.value()).build()
            , HttpStatus.OK);
      }
      DataPagingResponse<TradeHistoryResponse> data = tradeHistoryService
          .findAll(page, limit, search, status, sort, startDate, endDate);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
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
  @GetMapping("/find")
  public ResponseEntity<?> getAllByBatch(HttpServletRequest request,
      @RequestParam(name = "batch", required = true, defaultValue = "") String batch) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.ProductPermissionCode.PRODUCT_GET_BY_BATCH)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }

      List<TradeHistory> data = tradeHistoryService
          .findAllByBatch(batch);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
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

  @GetMapping("/find/{id}")
  public ResponseEntity<?> getAllByBatch(//HttpServletRequest request,
      @PathVariable("id") Integer id) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          PermissionObjectCode.ProductPermissionCode.PRODUCT_GET_BY_BATCH)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }

      CustomerTradeHistoryResponse data = tradeHistoryService
          .findAllByUserId(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
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


  @GetMapping("/find/bill-online")
  public ResponseEntity<?> getAllByBatch(
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "startDate", required = false) String startDate,
      @RequestParam(name = "endDate", required = false) String endDate) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          PermissionObjectCode.ProductPermissionCode.PRODUCT_GET_BY_BATCH)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }

      DataPagingResponse<CusTradeAddrInfo> data = tradeHistoryService
          .findAllBillOnline(page, limit, startDate, endDate);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
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

  @PostMapping("/sell")
  public ResponseEntity<?> sell(HttpServletRequest request,
      @RequestBody SellingProductRequest data) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.ProductPermissionCode.PRODUCT_SELLING)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }

      boolean result = businessService.sell(data);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build()
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
  @PostMapping("/sell-online")
  public ResponseEntity<?> sell(@RequestBody SellingOnlineRequest data) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          RoleCode.CUSTOMER)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }
      boolean result = businessService.sellOnline(data);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build()
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

  @PostMapping("/buy")
  public ResponseEntity<?> sell(HttpServletRequest request,
      @RequestBody BuyingProductRequest data) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          ProductPermissionCode.PRODUCT_BUYING)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }

      boolean result = businessService.buy(data);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build()
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

  @PostMapping("/revert")
  public ResponseEntity<?> revert(HttpServletRequest request,
      @RequestBody RevertRequestData data) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }

      boolean result = businessService.revert(data.getBatch());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build()
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
