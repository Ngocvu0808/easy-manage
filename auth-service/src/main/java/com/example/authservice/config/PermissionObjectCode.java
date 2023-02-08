package com.example.authservice.config;

/**
 * @author nguyen
 * @created_date 13/08/2020
 */
public interface PermissionObjectCode {

  String USER = "USER";
  String GROUP = "GROUP";
  String ROLE = "ROLE";
  String APPLICATION = "APPLICATION";
  String SERVICE = "SERVICE";

  interface RoleServicePermissionCode {

    String ROLE_ADD = "ROLE_ADD";
    String ROLE_UPDATE = "ROLE_UPDATE";
    String ROLE_DELETE = "ROLE_DELETE";
    String ROLE_GET_ALL = "ROLE_GET_ALL";
    String ROLE_GET_BY_ID = "ROLE_GET_BY_ID";
    String ROLE_GET_BY_TYPE = "ROLE_GET_BY_TYPE";
  }

  interface GroupServicePermissionCode {

    String GROUP_ADD = "GROUP_ADD";
    String GROUP_UPDATE = "GROUP_UPDATE";
    String GROUP_DELETE = "GROUP_DELETE";
    String GROUP_GET_BY_ID = "GROUP_GET_BY_ID";
    String GROUP_GET_ALL = "GROUP_GET_ALL";
    String GROUP_ADD_USER = "GROUP_ADD_USER";
    String GROUP_UPDATE_USER = "GROUP_UPDATE_USER";
    String GROUP_DELETE_USER = "GROUP_DELETE_USER";
    String GROUP_ADD_ROLE = "GROUP_ADD_ROLE";
    String GROUP_UPDATE_ROLE = "GROUP_UPDATE_ROLE";
    String GROUP_DELETE_ROLE = "GROUP_DELETE_ROLE";
    String GROUP_GET_ALL_USER = "GROUP_GET_ALL_USER";
    String GROUP_GET_ALL_ROLE = "GROUP_GET_ALL_ROLE";
  }

  interface UserServicePermissionCode {

    String USER_ADD = "USER_ADD";
    String USER_DELETE = "USER_DELETE";
    String USER_UPDATE = "USER_UPDATE";
    String USER_GET_BY_ID = "USER_GET_BY_ID";
    String USER_GET_ALL = "USER_GET_ALL";
    String USER_ADD_ROLES = "USER_ADD_ROLES";
    String USER_UPDATE_ROLES = "USER_UPDATE_ROLES";
    String USER_DELETE_ROLES = "USER_DELETE_ROLES";
    String USER_GET_ROLES = "USER_GET_ROLES";
    String USER_ENABLE = "USER_ENABLE";
    String USER_DISABLE = "USER_DISABLE";
    String USER_UPDATE_STATUS_LIST = "USER_UPDATE_STATUS_LIST";
    String USER_DELETE_LIST = "USER_DELETE_LIST";
    String USER_UPDATE_STATUS_ALL = "USER_UPDATE_STATUS_ALL";
    String USER_DELETE_ALL = "USER_DELETE_ALL";
    String USER_GET_ALL_PERMISSION = "USER_GET_ALL_PERMISSION";
    String USER_RESET_PASS = "USER_RESET_PASS";
    String DEVELOPER = "DEVELOPER";
    String USER_GET_EXIST_API_KEY = "USER_GET_EXIST_API_KEY";
    String USER_API_KEY_RELOAD = "USER_RELOAD_API_KEY";
    String USER_GET_ALL_TOKEN = "USER_GET_ALL_TOKEN";
    String USER_CHANGE_PASS = "USER_CHANGE_PASS";
  }

  interface ClientServiceCode {

    String CLIENT_GET_LIST_AUTH_CLIENT_API_KEY = "CLIENT_GET_LIST_AUTH_CLIENT_API_KEY";
    String CLIENT_GET_LIST_REFRESH_TOKEN = "CLIENT_GET_LIST_REFRESH_TOKEN";
    String CLIENT_GET_LIST_ACCESS_TOKEN = "CLIENT_GET_LIST_ACCESS_TOKEN";
    String CLIENT_GET_LIST_REQUEST_LOG = "CLIENT_GET_LIST_REQUEST_LOG";
    String CLIENT_EXPORT_REQUEST_LOG = "CLIENT_EXPORT_REQUEST_LOG";
    String CLIENT_GET_REQUEST_LOG = "CLIENT_GET_REQUEST_LOG";
    String CLIENT_ADD_USER = "CLIENT_ADD_USER";
    String CLIENT_ADD_SERVICE = "CLIENT_ADD_SERVICE";
    String CLIENT_ADD_API = "CLIENT_ADD_API";
    String CLIENT_DELETE_API = "CLIENT_DELETE_API";
    String CLIENT_GET_SERVICE = "CLIENT_GET_SERVICE";
    String CLIENT_GET_SERVICE_STATUS = "CLIENT_GET_SERVICE_STATUS";
    String CLIENT_GET_SERVICES = "CLIENT_GET_SERVICES";
    String CLIENT_SERVICE_GET_APIS = "CLIENT_SERVICE_GET_APIS";
    String CLIENT_PENDING_SERVICE = "CLIENT_PENDING_SERVICE";
    String CLIENT_DELETE_SERVICE = "CLIENT_DELETE_SERVICE";
    String CLIENT_DELETE_USER = "CLIENT_DELETE_USER";
    String CLIENT_GET_USERS = "CLIENT_GET_USERS";
    String CLIENT_UPDATE_ROLE_USER = "CLIENT_UPDATE_ROLE_USER";
    String CLIENT_UPDATE_STATUS_ACCESS_TOKEN = "CLIENT_UPDATE_STATUS_ACCESS_TOKEN";
    String CLIENT_ADD = "CLIENT_ADD";
    String CLIENT_GET_ALL = "CLIENT_GET_ALL";
    String CLIENT_GET_BY_ID = "CLIENT_GET_BY_ID";
    String CLIENT_DELETE = "CLIENT_DELETE";
    String CLIENT_UPDATE = "CLIENT_UPDATE";
    String CLIENT_CHANGE_STATUS = "CLIENT_CHANGE_STATUS";
    String CLIENT_ADD_IP = "CLIENT_ADD_IP";
    String CLIENT_DELETE_IP = "CLIENT_DELETE_IP";
    String CLIENT_GET_LIST_IP = "CLIENT_GET_LIST_IP";
    String CLIENT_APPROVE_TOKEN = "CLIENT_APPROVE_TOKEN";
    String CLIENT_UN_APPROVE_TOKEN = "CLIENT_UN_APPROVE_TOKEN";
    String CLIENT_DELETE_TOKEN = "CLIENT_DELETE_TOKEN";
    String CLIENT_CHANGE_STATUS_TOKEN = "CLIENT_CHANGE_STATUS_TOKEN";
    String CLIENT_CREATE_API_KEY = "CLIENT_CREATE_API_KEY";
    String CLIENT_GET_ALL_API_KEY = "CLIENT_GET_ALL_API_KEY";
    String CLIENT_DELETE_API_KEY = "CLIENT_DELETE_API_KEY";
    String CLIENT_CANCEL_API_KEY = "CLIENT_CANCEL_API_KEY";
    String CLIENT_REFRESH_API_KEY = "CLIENT_REFRESH_API_KEY";
  }

  interface ServiceServiceCode {

    String SERVICE_ADD = "SERVICE_ADD";
    String SERVICE_UPDATE = "SERVICE_UPDATE";
    String SERVICE_GET_ALL = "SERVICE_GET_ALL";
    String SERVICE_DELETE = "SERVICE_DELETE";
    String SERVICE_UPDATE_STATUS = "SERVICE_UPDATE_STATUS";
    String SERVICE_API_UPDATE_STATUS = "SERVICE_API_UPDATE_STATUS";
    String SERVICE_API_ADD = "SERVICE_API_ADD";
    String SERVICE_API_UPDATE = "SERVICE_API_UPDATE";
    String SERVICE_API_GET_BY_ID = "SERVICE_API_GET_BY_ID";
    String SERVICE_API_DELETE = "SERVICE_API_DELETE";
    String SERVICE_API_GET_ALL = "SERVICE_API_GET_ALL";
    String SERVICE_API_REQUEST_GET_ALL = "SERVICE_API_REQUEST_GET_ALL";
    String SERVICE_API_REQUEST_GET_BY_ID = "SERVICE_API_REQUEST_GET_BY_ID";
    String SERVICE_API_REQUEST_UPDATE = "SERVICE_API_REQUEST_UPDATE";
    String SERVICE_GET_BY_ID = "SERVICE_GET_BY_ID";
  }
}
