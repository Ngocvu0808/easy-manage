package com.example.authservice.service.iface;

import com.example.authservice.utils.exception.ProxyAuthenticationException;
import com.example.authservice.utils.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public interface AuthGuardService {
  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode, String permissionCode) throws ProxyAuthenticationException;

  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode, List<String> permissionCode) throws ProxyAuthenticationException;

  Integer getUserId(HttpServletRequest request) throws UserNotFoundException, ProxyAuthenticationException, UserNotFoundException;

  void checkAuthorization(HttpServletRequest request) throws ProxyAuthenticationException;

  Boolean checkPermissionByJwt(String jwt, Integer objectId, String objectCode, String permissionCode) throws ProxyAuthenticationException, ProxyAuthenticationException;

}
