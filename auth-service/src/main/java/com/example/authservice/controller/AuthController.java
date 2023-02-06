package com.example.authservice.controller;


import com.example.authservice.config.Constants;
import com.example.authservice.dto.auth.LoginRequestDto;
import com.example.authservice.dto.auth.LoginResponseDto;
import com.example.authservice.service.iface.AuthService;
import com.example.authservice.utils.exception.CryptoException;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.exception.UnAuthorizedException;
import com.example.authservice.utils.response.BaseMethodResponse;
import com.example.authservice.utils.response.GetMethodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.Arrays;


@RestController
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody LoginRequestDto dataLogin) {
    try {
      LoginResponseDto data = authService.login(request, dataLogin);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (CryptoException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(Arrays.toString(e.getStackTrace()));
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/check", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> checkAuthenticate(HttpServletRequest request) {
    try {
      LoginResponseDto responseData = authService.checkAuthenticate(request);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(responseData).message(Constants.SUCCESS_MSG)
              .httpCode(HttpStatus.OK.value()).errorCode(HttpStatus.OK.name().toLowerCase()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.UNAUTHORIZED.name().toLowerCase())
              .httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @GetMapping(path = "/validate", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> validateToken(HttpServletRequest request) {
    try {
      Boolean responseData = authService.validateToken(request);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(responseData).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(false).data(false)
              .message(e.getMessage()).errorCode(HttpStatus.UNAUTHORIZED.name().toLowerCase())
              .httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @PostMapping(path = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> logOut(HttpServletRequest request) {
    try {
      authService.logout(request);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ServletException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }


}
