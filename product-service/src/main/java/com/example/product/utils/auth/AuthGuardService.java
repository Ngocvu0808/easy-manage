package com.example.product.utils.auth;


import com.example.product.utils.exception.ProxyAuthenticationException;
import com.example.product.utils.exception.UnAuthorizedException;
import com.example.product.utils.exception.UserNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public interface AuthGuardService {

  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, UnAuthorizedException;

  Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      List<String> permissionCode) throws ProxyAuthenticationException;

  Integer getUserId(HttpServletRequest request)
      throws UserNotFoundException, ProxyAuthenticationException, UserNotFoundException;

  void checkAuthorization(HttpServletRequest request) throws ProxyAuthenticationException;

  Boolean checkPermissionByJwt(String jwt, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, ProxyAuthenticationException;
  String getUsernameFromToken(HttpServletRequest request) throws UnAuthorizedException;

}
