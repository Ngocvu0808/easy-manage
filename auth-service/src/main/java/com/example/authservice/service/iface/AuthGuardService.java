package com.example.authservice.service.iface;

import com.example.authservice.utils.exception.ProxyAuthenticationException;
import com.example.authservice.utils.exception.UnAuthorizedException;
import com.example.authservice.utils.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public interface AuthGuardService {

  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, UnAuthorizedException;

  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      List<String> permissionCode) throws ProxyAuthenticationException, UnAuthorizedException;

  Integer getUserId(HttpServletRequest request)
      throws UserNotFoundException, ProxyAuthenticationException, UserNotFoundException, UnAuthorizedException;

  void checkAuthorization(HttpServletRequest request)
      throws ProxyAuthenticationException, UnAuthorizedException;

  Boolean checkPermissionByJwt(String jwt, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, ProxyAuthenticationException;

}
