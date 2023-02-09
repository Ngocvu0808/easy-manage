package com.example.business.utils;

/**
 * @author nguyen
 * @created_date 04/06/2020
 */
public final class KeyConstants {

  public interface Headers {

    String X_API_KEY = "x-api-key";
    String ACCESS_TOKEN = "access-token";
    String TOKEN = "token";
  }

  public interface RedisKey {

    String AUTH_APP = "auth:app:";
    String AUTH_TOKEN = "auth:token:";
    String JWT = "jwt";
    String APPROVED = "approved";
    String REFRESH_TOKEN = "refresh_token";
    String ACCESS_EXP = "access_exp";
    String REFRESH_EXP = "refresh_exp";
    String CLIENT_ID = "client_id";
    String IP_WHITELIST = "ip_whitelist";
  }

  public interface JSONKey {

    String CLIENT_ID = "client_id";
    String USER_ID = "user_id";
    String SUB = "sub";
    String TYPE = "type";
    String JTI = "jti";
    String PREFERRED_USERNAME = "preferred_username";
    String EMAIL = "email";
    String NAME = "name";
    String PERMISSIONS = "permissions";
    String ACCESS_TOKEN = "access_token";
    String ACCESS_TOKEN_EXP = "access_token_exp";
    String REFRESH_TOKEN = "refresh_token";
    String REFRESH_TOKEN_EXP = "refresh_token_exp";
    String SERVICE = "service";
    String USER_TYPE = "user-type";
  }
}
