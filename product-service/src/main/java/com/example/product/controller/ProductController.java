package com.example.product.controller;

import com.example.product.config.Constants;
import com.example.product.config.MessageCode;
import com.example.product.config.PermissionObjectCode;
import com.example.product.dto.request.AddProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
import com.example.product.dto.response.GetProductResponse;
import com.example.product.service.iface.ProductService;
import com.example.product.utils.DateUtil;
import com.example.product.utils.ServiceInfo;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.auth.AuthGuardService;
import com.example.product.utils.exception.ProxyAuthenticationException;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.exception.UserNotFoundException;
import com.example.product.utils.response.BaseMethodResponse;
import com.example.product.utils.response.DataPagingResponse;
import com.example.product.utils.response.GetMethodResponse;
import com.example.product.utils.response.PostMethodResponse;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProductController.class);
  private final AuthGuardService authGuard;
  private final ProductService productService;

  public ProductController(AuthGuardService authGuard, ProductService productService) {
    this.authGuard = authGuard;
    this.productService = productService;
  }

  @GetMapping("")
  public ResponseEntity<?> getAll(HttpServletRequest request,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
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
      DataPagingResponse<GetProductResponse> data = productService
          .getAll(page, limit, search, status, sort);
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

  @PostMapping("/app")
  public ResponseEntity<?> add(HttpServletRequest request,
      @RequestBody AddProductRequest requestDto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      int result = productService.add(requestDto);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(result)
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

  @GetMapping("/{id}")
  public ResponseEntity<?> add(HttpServletRequest request,
      @PathVariable("id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      GetProductResponse result = productService.findOne(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
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

  @PutMapping("/disable/{id}")
  public ResponseEntity<?> disable(HttpServletRequest request,
      @PathVariable("id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      boolean result = productService.delete(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
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

  @PutMapping("")
  public ResponseEntity<?> update(HttpServletRequest request,
      @RequestBody UpdateProductRequest requestData) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      boolean result = productService.update(requestData);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build()
          , HttpStatus.OK);
    } catch(Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }


  @GetMapping("/all-value")
  public ResponseEntity<?> getAllValueProduct(HttpServletRequest request,
      @RequestParam(name = "time", required = false, defaultValue = "") Long time) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          PermissionObjectCode.UserServicePermissionCode.DEVELOPER)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      if (time == null)
        time = DateUtil.getOnlyDateFromTimeStamp(new Date().getTime());
      long result = productService.getAllValueProduct(time);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch(Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }
}
