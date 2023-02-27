package com.example.business.utils.auth;


import com.example.business.dto.response.LoginResponseDto;
import com.example.business.utils.exception.ProxyAuthenticationException;
import com.example.business.utils.exception.UnAuthorizedException;
import com.example.business.utils.exception.UserNotFoundException;
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
      List<String> permissionCode) throws ProxyAuthenticationException, UnAuthorizedException;

  Integer getUserId(HttpServletRequest request)
      throws UserNotFoundException, ProxyAuthenticationException, UserNotFoundException, UnAuthorizedException;

  void checkAuthorization(HttpServletRequest request)
      throws ProxyAuthenticationException, UnAuthorizedException;

  Boolean checkPermissionByJwt(String jwt, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, ProxyAuthenticationException;

}
