package com.example.product.controller;

import com.example.product.config.Constants;
import com.example.product.config.PermissionObjectCode;
import com.example.product.config.PermissionObjectCode.RoleCode;
import com.example.product.dto.request.AddCartRequest;
import com.example.product.dto.response.GetCartByUserIdResponse;
import com.example.product.service.iface.CartService;
import com.example.product.utils.auth.AuthGuardService;
import com.example.product.utils.response.BaseMethodResponse;
import com.example.product.utils.response.GetMethodResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cart")
public class CartController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final CartService cartService;
  private final AuthGuardService authGuard;


  public CartController(CartService cartService, AuthGuardService authGuard) {
    this.cartService = cartService;
    this.authGuard = authGuard;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getCartByUserId(HttpServletRequest request,
      @PathVariable("userId") int id) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          RoleCode.CUSTOMER)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }
      GetCartByUserIdResponse result = cartService.getCartByUserId(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }


  @PostMapping("")
  public ResponseEntity<?> addCart(HttpServletRequest request,
      @RequestBody AddCartRequest addCartRequest) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          RoleCode.CUSTOMER)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }
      int result = cartService.addCart(addCartRequest);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

}