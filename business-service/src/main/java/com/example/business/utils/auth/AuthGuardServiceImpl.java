package com.example.business.utils.auth;

import com.example.business.config.Constants;
import com.example.business.config.MessageCode;
import com.example.business.utils.EncodeUtils;
import com.example.business.utils.JsonUtils;
import com.example.business.utils.KeyConstants;
import com.example.business.utils.KeyConstants.Headers;
import com.example.business.utils.KeyConstants.RedisKey;
import com.example.business.utils.ServiceInfo;
import com.example.business.utils.cache.CacheRedisService;
import com.example.business.utils.exception.ProxyAuthenticationException;
import com.example.business.utils.exception.UnAuthorizedException;
import com.example.business.utils.exception.UserNotFoundException;
import com.example.business.utils.permission.ObjectPermission;
import com.example.business.utils.permission.Permission;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */

@Service
public class AuthGuardServiceImpl implements AuthGuardService {
  private final Logger logger = LoggerFactory.getLogger(AuthGuardServiceImpl.class);

  private final String MISSING_TOKEN_CODE = "0000";
  private final String BASE_CODE = "00";
  private final CacheRedisService redisService;
  public AuthGuardServiceImpl(CacheRedisService redisService) {
    this.redisService = redisService;
  }

  public Boolean checkPermissionByJwt(String token, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException {
    if (token.isBlank()) {
      throw new ProxyAuthenticationException("Missing token", "000000");
    } else {
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
          if (generalPermissions.contains(permissionCode)) {
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
                  List<String> permissionOfObject = new ArrayList<>();
                  objectPermissionList.forEach((it) -> {
                    it.getPermissionList().stream().filter((el) -> {
                      return el.getId().equals(objectId);
                    }).forEach((l) -> {
                      permissionOfObject.addAll(l.getPermissions());
                    });
                  });
                  return permissionOfObject.size() > 0 && permissionOfObject.contains(
                      permissionCode);
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

  public Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      String permissionCode) throws ProxyAuthenticationException, UnAuthorizedException {
    String token = request.getHeader("Authorization");
    if (token != null && !token.isEmpty()) {
      return this.checkPermissionByJwt(token, objectId, objectCode, permissionCode);
    }
    String apiKey = request.getHeader(Headers.X_API_KEY);
    if (apiKey != null && !apiKey.isEmpty()) {
      Map<String, String> res = this.getJwtOfApiKey(apiKey);
      if (res != null && !res.isEmpty()) {
        token = res.get(RedisKey.JWT);
        // check IP
        String ipRequest = request.getHeader(Constants.AGENT);
        logger.info("ip request: {}", ipRequest);
        if (ipRequest.equals(Constants.AGENT)) {
          return this.checkPermissionByJwt(token, objectId, objectCode, permissionCode);
        }
        if (ipRequest == null || ipRequest.isBlank()) {
          throw new UnAuthorizedException("ip request is null or empty",
              ServiceInfo.getId() + MessageCode.IP_INVALID);
        }
        String ipWhiteList = res.get(RedisKey.IP_WHITELIST);
        if (ipWhiteList == null || ipWhiteList.isBlank()) {
          logger.info("ip whitelist is null or empty");
          throw new UnAuthorizedException("client api key invalid",
              ServiceInfo.getId() + MessageCode.CLIENT_API_KEY_INVALID);
        }
        List<String> listIpValid = Arrays
            .asList(JsonUtils.gson().fromJson(ipWhiteList, String[].class));
        if (listIpValid.isEmpty()) {
          logger.info("ip whitelist is empty");
          throw new UnAuthorizedException("client api key invalid",
              ServiceInfo.getId() + MessageCode.CLIENT_API_KEY_INVALID);
        }
        if (!listIpValid.contains(ipRequest.trim())) {
          throw new UnAuthorizedException("ip request invalid",
              ServiceInfo.getId() + MessageCode.IP_INVALID);
        }
      }
    }
    return this.checkPermissionByJwt(token, objectId, objectCode, permissionCode);
  }
  Map<String, String> getJwtOfApiKey(String apiKey) {
    Map<String, String> map = new HashMap<>();
    String key = KeyConstants.RedisKey.AUTH_APP.concat(apiKey);

    if (redisService.exists(key)) {
      String jwt = redisService.hGet(key, KeyConstants.RedisKey.JWT).toString();
      String ipList = redisService.hGet(key, KeyConstants.RedisKey.IP_WHITELIST).toString();
      map.put(KeyConstants.RedisKey.IP_WHITELIST, ipList);
      map.put(KeyConstants.RedisKey.JWT, jwt);
    }
    return map;
  }
  public Boolean checkPermission(HttpServletRequest request, Integer objectId, String objectCode,
      List<String> permissionCode) throws ProxyAuthenticationException {
    String token = request.getHeader("Authorization");
    if (token != null && !token.isBlank()) {
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
                  .collect(Collectors.toSet());
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
    } else {
      throw new ProxyAuthenticationException("Missing token", "000000");
    }
  }

  public Integer getUserId(HttpServletRequest request)
      throws ProxyAuthenticationException, UserNotFoundException {
    String token = request.getHeader("Authorization");
    if (token != null && !token.isBlank()) {
      String jwtValue = EncodeUtils.decodeJWT(token);
      if (jwtValue == null) {
        throw new ProxyAuthenticationException("Token invalid", "000000");
      } else {
        JSONObject jsonObject = new JSONObject(jwtValue);
        if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
          return (Integer) jsonObject.get("user_id");
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
