package com.example.authservice.controller;


import com.example.authservice.config.Constants;
import com.example.authservice.config.PermissionObjectCode;
import com.example.authservice.config.PermissionObjectCode.RoleCode;
import com.example.authservice.dto.app.RefreshTokenDto;
import com.example.authservice.dto.auth.ChangePassRequestDto;
import com.example.authservice.dto.auth.DeleteUserListDto;
import com.example.authservice.dto.auth.LoginResponseDto;
import com.example.authservice.dto.auth.RegisterRequestDto;
import com.example.authservice.dto.auth.UpdateStatusUserListDto;
import com.example.authservice.dto.filter.UserStatusDto;
import com.example.authservice.dto.group.GroupAndUserDto;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.dto.user.GetCustomerResponse;
import com.example.authservice.dto.user.RegisterCustomerRequest;
import com.example.authservice.dto.user.UpdateCustomerRequest;
import com.example.authservice.dto.user.UserActivityResponse;
import com.example.authservice.dto.user.UserDto;
import com.example.authservice.entities.UserStatus;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.service.iface.AuthGuardService;
import com.example.authservice.service.iface.AuthService;
import com.example.authservice.service.iface.UserService;
import com.example.authservice.utils.ServiceInfo;
import com.example.authservice.utils.SortingUtils;
import com.example.authservice.utils.exception.*;
import com.example.authservice.utils.permission.Permission;
import com.example.authservice.utils.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final AuthGuardService authGuard;
  private final UserService userService;
  private final AuthService authService;

  public UserController(AuthGuardService authGuard, UserService userService,
      AuthService authService) {
    this.authGuard = authGuard;
    this.userService = userService;
    this.authService = authService;
  }

  @PostMapping(path = "/user/")
  public ResponseEntity<?> addNewUser(HttpServletRequest request,
      @RequestBody RegisterRequestDto registerRequestDto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      UserDto responseDto = userService.createUser(registerRequestDto, uid);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(responseDto.getId())
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
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

  @PutMapping(path = "/user/{id}/enable")
  public ResponseEntity<?> enableUser(HttpServletRequest request, @PathVariable("id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_ENABLE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      UserDto dto = userService.updateStatusUser(id, uid, UserStatus.ACTIVE);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).id(dto.getId()).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @PutMapping(path = "/user/{id}/disable")
  public ResponseEntity<?> disableUser(HttpServletRequest request, @PathVariable("id") Integer id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_DISABLE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      if (uid.equals(id)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      UserDto dto = userService.updateStatusUser(id, uid, UserStatus.DEACTIVE);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).id(dto.getId()).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @PutMapping(path = "/users/status/")
  public ResponseEntity<?> updateStatusUsers(HttpServletRequest request,
      @RequestBody UpdateStatusUserListDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_UPDATE_STATUS_LIST)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      userService.updateStatusListUser(dto, uid);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @PutMapping(path = "/users/delete/")
  public ResponseEntity<?> deleteUsers(HttpServletRequest request,
      @RequestBody DeleteUserListDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_DELETE_LIST)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      userService.deleteUserList(dto, uid);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @DeleteMapping(path = "/user/{id}")
  public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable("id") Integer id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      if (uid.equals(id)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      userService.deleteUser(id, uid);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping(path = "/user/{id}/roles")
  public ResponseEntity<?> getRoleUser(HttpServletRequest request,
      @PathVariable("id") Integer userId) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_GET_ROLES)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      List<RoleDto> roles = userService.getRolesUser(userId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(roles)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @GetMapping(path = "/user/{id}/permissions")
  public ResponseEntity<?> getPermissionUser(HttpServletRequest request,
      @PathVariable("id") Integer userId) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_GET_ALL_PERMISSION)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Permission permissions = userService.getPermissionsOfUser(userId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(permissions)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
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
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/user/{id}/roles")
  public ResponseEntity<?> addRoleUser(HttpServletRequest request, @PathVariable("id") Integer id,
      @RequestBody List<Integer> roleIds) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_ADD_ROLES)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      userService.addRoleUser(id, uid, roleIds);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @DeleteMapping(path = "/user/{id}/roles")
  public ResponseEntity<?> deleteRoleUser(HttpServletRequest request,
      @PathVariable("id") Integer id, @RequestBody List<Integer> roleIds) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_DELETE_ROLES)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      userService.deleteRoleUser(id, uid, roleIds);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase()).build()
          , HttpStatus.OK);
    }
  }

  @PutMapping(path = "/user/{id}/roles")
  public ResponseEntity<?> updateRoleUser(HttpServletRequest request,
      @PathVariable("id") Integer id, @RequestBody List<Integer> roleIds) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_UPDATE_ROLES)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      userService.updateRoleUser(id, uid, roleIds);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping(path = "/user/{id}")
  public ResponseEntity<?> getUser(HttpServletRequest request, @PathVariable("id") Integer id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      LoginResponseDto loginResponseDto = authService.checkAuthenticate(request);
      String token = loginResponseDto.getJwt();
      UserDto userDetail = userService.getUserById(token, id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(userDetail)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
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

  @GetMapping("/users")
  public ResponseEntity<?> getAllUser(HttpServletRequest request,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "status", required = false, defaultValue = "") String status,
      @RequestParam(name = "roles", required = false, defaultValue = "") String roles,
      @RequestParam(name = "groups", required = false, defaultValue = "") String groups,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.USER,
              PermissionObjectCode.UserServicePermissionCode.USER_GET_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      authGuard.checkAuthorization(request);
      if (!SortingUtils.validateSort(sort, UserDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(ServiceInfo.getId() + AuthServiceMessageCode.SORT_INVALID)
                .httpCode(HttpStatus.BAD_REQUEST.value()).build()
            , HttpStatus.OK);
      }
      DataPagingResponse<UserDto> userList = userService
          .getAll(page, limit, status, roles, groups, search, sort);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(userList)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @GetMapping("/users/roles")
  public ResponseEntity<?> getAllRoleAssignedUser(HttpServletRequest request) {
    try {
      authGuard.checkAuthorization(request);
      List<RoleCustomDto> listRoleUserDto = userService.getAllRoleAssignedUser();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(listRoleUserDto)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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
//
//  @GetMapping("/users/groups")
//  public ResponseEntity<?> getAllGroupAssignedUser(HttpServletRequest request) {
//    try {
//      authGuard.checkAuthorization(request);
//      List<GroupUserCustomResponseDto> listGroupUser = userService.getAllGroupAssignedUser();
//      return new ResponseEntity<>(
//          GetMethodResponse.builder().status(true).data(listGroupUser)
//              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
//              .httpCode(HttpStatus.OK.value()).build()
//          , HttpStatus.OK);
//    } catch (ProxyAuthenticationException e) {
//      logger.warn(e.getMessage());
//      return new ResponseEntity<>(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
//          , HttpStatus.OK);
//    } catch (Exception e) {
//      logger.error(e.getMessage());
//      return new ResponseEntity<>(
//          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
//              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
//              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
//          , HttpStatus.OK);
//    }
//  }

  @GetMapping("/user/user-and-group")
  public ResponseEntity<?> findAllGroupAndUser(HttpServletRequest request,
      @RequestParam(value = "filter", defaultValue = "") String filter) {
    try {
      authGuard.checkAuthorization(request);
      List<GroupAndUserDto> listGroupAndUser = userService
          .getAllGroupAndUser(filter);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(listGroupAndUser)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @PutMapping("/user/{id}")
  public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable("id") Integer id,
      @RequestBody @Valid UserDto userDto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_UPDATE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      userDto.setId(id);
      UserDto user = userService.updateUser(userDto, userId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).id(user.getId())
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/user/status")
  public ResponseEntity<?> getListStatus(HttpServletRequest request) {
    try {
      authGuard.checkAuthorization(request);
      List<UserStatusDto> userStatusDtos = userService.getListStatusUser();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(userStatusDtos)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @PutMapping("/users/status/all")
  public ResponseEntity<?> changeStatusAll(HttpServletRequest request,
      @RequestBody UpdateStatusUserListDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_UPDATE_STATUS_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      userService.updateStatusAll(userId, dto.getStatus());
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @DeleteMapping("/users/")
  public ResponseEntity<?> deleteAll(HttpServletRequest request) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_DELETE_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      userService.deleteAll(userId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @PutMapping(path = "/user/{id}/reset-pass", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> resetPassword(HttpServletRequest request,
      @PathVariable("id") Integer userId) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_RESET_PASS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer updaterId = authGuard.getUserId(request);
      UserDto responseData = userService.resetPassword(userId, updaterId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .id(responseData.getId()).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @PutMapping("/change-pass")
  public ResponseEntity<?> changePassword(HttpServletRequest request,
      @RequestBody ChangePassRequestDto changePassRequestDto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.USER,
          PermissionObjectCode.UserServicePermissionCode.USER_CHANGE_PASS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      UserDto user = userService.changePass(userId, changePassRequestDto);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).id(user.getId())
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
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
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/api-key-generate")
  public ResponseEntity<?> apiKeyGen(HttpServletRequest request) {
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
      String apiKey = authService.genAPIKey(userId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(apiKey)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/api-key")
  public ResponseEntity<?> getAPIKeyOfUser(HttpServletRequest request) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          Arrays.asList(PermissionObjectCode.UserServicePermissionCode.USER_GET_EXIST_API_KEY,
              PermissionObjectCode.UserServicePermissionCode.DEVELOPER))) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      String apiKey = authService.getExistAPIKey(userId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(apiKey)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/api-key/reload")
  public ResponseEntity<?> refreshApiKey(HttpServletRequest request) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          Arrays.asList(PermissionObjectCode.UserServicePermissionCode.USER_API_KEY_RELOAD,
              PermissionObjectCode.UserServicePermissionCode.DEVELOPER))) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      String apiKey = authService.reloadAPIKey(userId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(apiKey)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/tokens")
  public ResponseEntity<?> getAllToken(HttpServletRequest request,
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "status", required = false, defaultValue = "0") String status,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort) {

    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          Arrays.asList(PermissionObjectCode.UserServicePermissionCode.USER_GET_ALL_TOKEN,
              PermissionObjectCode.UserServicePermissionCode.DEVELOPER))) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, RefreshTokenDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build()
            , HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      DataPagingResponse<RefreshTokenDto> tokens = authService
          .getAllRefreshTokenOfUser(userId, page, limit, status, sort);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(tokens)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UserNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
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

  @GetMapping("/user-session")
  public ResponseEntity<?> userSession(HttpServletRequest request,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "startDate", required = false) String startDate,
      @RequestParam(name = "endDate", required = false) String endDate,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {

    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
          Arrays.asList(PermissionObjectCode.UserServicePermissionCode.USER_GET_ALL_TOKEN,
              PermissionObjectCode.UserServicePermissionCode.DEVELOPER))) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, RefreshTokenDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build()
            , HttpStatus.OK);
      }
      DataPagingResponse<UserActivityResponse> result = userService
          .getAllUserSession(page, limit, status, search, sort, startDate, endDate);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
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

  @GetMapping("/customer/{id}")
  public ResponseEntity<?> customer(HttpServletRequest request, @PathVariable("id") int id) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.APPLICATION,
//          Arrays.asList(PermissionObjectCode.UserServicePermissionCode.USER_GET_ALL_TOKEN,
//              RoleCode.CUSTOMER))) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build()
//            , HttpStatus.OK);
//      }
      GetCustomerResponse result = userService.getCustomer(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
//    } catch (ProxyAuthenticationException e) {
//      logger.warn(e.getMessage());
//      return new ResponseEntity<>(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
//          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping("/customer")
  public ResponseEntity<?> customer(HttpServletRequest request, @RequestBody RegisterCustomerRequest requestData) {
    try {
      int result = userService.addCustomer(requestData);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
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

  @GetMapping("customer")
  public ResponseEntity<?> customer(HttpServletRequest request,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "search", required = false, defaultValue = "") String search) {
    try {
      DataPagingResponse<GetCustomerResponse>  result = userService.getCustomerList(page, limit, search);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
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
  @PutMapping("/customer/{id}")
  public ResponseEntity<?> customer(@PathVariable("id") Integer id,
      @RequestBody UpdateCustomerRequest requestData) {
    try {
      int result = userService.updateCustomer(requestData, id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
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