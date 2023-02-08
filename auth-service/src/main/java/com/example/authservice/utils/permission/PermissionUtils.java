package com.example.authservice.utils.permission;

import com.example.authservice.utils.exception.ProxyAuthenticationException;
import com.example.authservice.utils.EncodeUtils;
import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class PermissionUtils {

  public static Boolean checkPermission(HttpServletRequest request, Integer objectId,
      String objectCode, String permissionCode) throws ProxyAuthenticationException {
    String token = getTokenFromRequest(request);
    return checkPermission(token, objectId, objectCode, Collections.singletonList(permissionCode));
  }

  private static String getTokenFromRequest(HttpServletRequest request)
      throws ProxyAuthenticationException {
    checkMissingAuthHeader(request);
    String token = request.getHeader("Authorization");
    if (token != null && !token.isBlank()) {
      return token;
    } else {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }

  private static void checkMissingAuthHeader(HttpServletRequest request)
      throws ProxyAuthenticationException {
    Set<String> headers = new HashSet(Collections.list(request.getHeaderNames()));
    if (!headers.contains("Authorization".toLowerCase(Locale.ROOT))) {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }

  private static boolean checkPermission(String token, Integer objectId, String objectCode,
      List<String> permissionCode) {
    String jwtValue = EncodeUtils.decodeJWT(token);
    if (jwtValue == null) {
      return false;
    } else {
      JSONObject jsonObject = new JSONObject(jwtValue);
      if (jsonObject.has("permissions") && !jsonObject.isNull("permissions") && !String.valueOf(
          jsonObject.get("permissions")).isBlank()) {
        Permission permission = (Permission) (new Gson()).fromJson(
            jsonObject.getString("permissions"), Permission.class);
        List<String> generalPermissions = permission.getGeneralPermissions();
        if (generalPermissions.containsAll(permissionCode)) {
          return true;
        } else {
          List<ObjectPermission> specificPermission = permission.getSpecificPermissions();
          if (specificPermission != null && !specificPermission.isEmpty()) {
            Set<String> keyList = (Set) specificPermission.stream().map(ObjectPermission::getName)
                .collect(
                    Collectors.toSet());
            if (!keyList.contains(objectCode)) {
              return false;
            } else if (objectId != null) {
              List<ObjectPermission> objectPermissionList = (List) specificPermission.stream()
                  .filter((it) -> {
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
  }
}
