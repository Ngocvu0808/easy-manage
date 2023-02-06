package com.example.authservice.service.impl;

import com.example.authservice.utils.exception.ProxyAuthenticationException;
import com.example.authservice.utils.exception.UserNotFoundException;
import com.example.authservice.service.iface.AuthGuardService;
import com.example.authservice.utils.EncodeUtils;
import com.example.authservice.utils.permission.ObjectPermission;
import com.example.authservice.utils.permission.Permission;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */

@Service
public class AuthGuardServiceImpl implements AuthGuardService {

  private final String MISSING_TOKEN_CODE = "0000";
  private final String BASE_CODE = "00";

  public AuthGuardServiceImpl() {
  }

  public Boolean checkPermissionByJwt(String token, Integer objectId, String objectCode, String permissionCode) throws ProxyAuthenticationException {
    if (token.isBlank()) {
      throw new ProxyAuthenticationException("Missing token", "000000");
    } else {
      String jwtValue = EncodeUtils.decodeJWT(token);
      if (jwtValue == null) {
        return false;
      } else {
        JSONObject jsonObject = new JSONObject(jwtValue);
        if (jsonObject.has("permissions") && !jsonObject.isNull("permissions") && !String.valueOf(jsonObject.get("permissions")).isBlank()) {
          Permission permission = (Permission)(new Gson()).fromJson(jsonObject.getString("permissions"), Permission.class);
          List<String> generalPermissions = permission.getGeneralPermissions();
          if (generalPermissions.contains(permissionCode)) {
            return true;
          } else {
            List<ObjectPermission> specificPermission = permission.getSpecificPermissions();
            if (specificPermission != null && !specificPermission.isEmpty()) {
              Set<String> keyList = (Set)specificPermission.stream().map(ObjectPermission::getName).collect(
                  Collectors.toSet());
              if (!keyList.contains(objectCode)) {
                return false;
              } else if (objectId != null) {
                List<ObjectPermission> objectPermissionList = (List)specificPermission.stream().filter((it) -> {
                  return it.getName().equals(objectCode);
                }).collect(Collectors.toList());
                if (objectPermissionList.isEmpty()) {
                  return false;
                } else {
                  List<String> permissionOfObject = new ArrayList<>();
                  objectPermissionList.forEach((it) -> {
                    it.getPermissionList().stream().filter((el) -> {
                      return el.getId().equals(objectId);
                    }).forEach((l) -> {
                      permissionOfObject.addAll(l.getPermissions());
                    });
                  });
                  return permissionOfObject.size() > 0 && permissionOfObject.contains(permissionCode);
                }
              } else {
                return false;
              }
            } else {
              return false;
            }
          }
        } else {
          return false;
        }
      }
    }
  }

  public Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode, String permissionCode) throws ProxyAuthenticationException {
    String token = request.getHeader("Authorization");
    return this.checkPermissionByJwt(token, objectId, objectCode, permissionCode);
  }

  public Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode, List<String> permissionCode) throws ProxyAuthenticationException {
    String token = request.getHeader("Authorization");
    if (token != null && !token.isBlank()) {
      String jwtValue = EncodeUtils.decodeJWT(token);
      if (jwtValue == null) {
        return false;
      } else {
        JSONObject jsonObject = new JSONObject(jwtValue);
        if (jsonObject.has("permissions") && !jsonObject.isNull("permissions") && !String.valueOf(jsonObject.get("permissions")).isBlank()) {
          Permission permission = (Permission)(new Gson()).fromJson(jsonObject.getString("permissions"), Permission.class);
          List<String> generalPermissions = permission.getGeneralPermissions();
          if (generalPermissions.containsAll(permissionCode)) {
            return true;
          } else {
            List<ObjectPermission> specificPermission = permission.getSpecificPermissions();
            if (specificPermission != null && !specificPermission.isEmpty()) {
              Set<String> keyList = (Set)specificPermission.stream().map(ObjectPermission::getName).collect(Collectors.toSet());
              if (!keyList.contains(objectCode)) {
                return false;
              } else if (objectId != null) {
                List<ObjectPermission> objectPermissionList = (List)specificPermission.stream().filter((it) -> {
                  return it.getName().equals(objectCode);
                }).collect(Collectors.toList());
                if (objectPermissionList.isEmpty()) {
                  return false;
                } else {
                  List<String> permissionOfObject = new ArrayList();
                  objectPermissionList.forEach((it) -> {
                    it.getPermissionList().stream().filter((el) -> {
                      return el.getId().equals(objectId);
                    }).forEach((l) -> {
                      permissionOfObject.addAll(l.getPermissions());
                    });
                  });
                  return permissionOfObject.containsAll(permissionCode);
                }
              } else {
                return false;
              }
            } else {
              return false;
            }
          }
        } else {
          return false;
        }
      }
    } else {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }

  public Integer getUserId(HttpServletRequest request) throws ProxyAuthenticationException, UserNotFoundException {
    String token = request.getHeader("Authorization");
    if (token != null && !token.isBlank()) {
      String jwtValue = EncodeUtils.decodeJWT(token);
      if (jwtValue == null) {
        throw new ProxyAuthenticationException("Token invalid", "000000");
      } else {
        JSONObject jsonObject = new JSONObject(jwtValue);
        if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
          return (Integer)jsonObject.get("user_id");
        } else {
          throw new UserNotFoundException("User does not exist");
        }
      }
    } else {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }

  public void checkAuthorization(HttpServletRequest request) throws ProxyAuthenticationException {
    String token = request.getHeader("Authorization");
    if (token == null || token.isBlank()) {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }
}
