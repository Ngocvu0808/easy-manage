package com.example.authservice.service.iface;


import com.example.authservice.dto.app.*;
import com.example.authservice.dto.appservice.ApiClientServiceDto;
import com.example.authservice.dto.appservice.ClientApiAddDto;
import com.example.authservice.dto.appservice.ClientServiceAddDto;
import com.example.authservice.dto.filter.ClientAuthTypeDto;
import com.example.authservice.dto.filter.LogRequestStatusDto;
import com.example.authservice.dto.refreshtoken.AccessTokenResponseDto;
import com.example.authservice.dto.refreshtoken.AccessTokenStatusRequestDto;
import com.example.authservice.dto.refreshtoken.RefreshTokenResponseDto;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.service.CustomServiceDto;
import com.example.authservice.dto.service.ServiceResponseDto;
import com.example.authservice.entities.enums.RefreshTokenStatus;
import com.example.authservice.entities.enums.ServiceStatus;
import com.example.authservice.entities.enums.TokenStatus;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ApplicationService {

  List<RefreshTokenResponseDto> getRefreshTokensByClientId(RefreshTokenStatus status, Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  void addUser(Integer clientId, Integer creatorUserId, ClientUserAddRequestDto requestDto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  List<AccessTokenResponseDto> getAccessTokenByClientId(TokenStatus status, Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  List<UserClientCustomDto> getUsers(Integer clientId, String roles, String search, String sort)
      throws ResourceNotFoundException;

  void deleteUser(Integer clientId, Integer userId, Integer deleterUserId)
      throws ResourceNotFoundException;

  void updateRoleUser(Integer clientId, Integer updaterUserId, ClientUserAddRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<RoleCustomDto> getListRoleAssigned(Integer clientId) throws ResourceNotFoundException;

  void updateStatusOfAccessToken(AccessTokenStatusRequestDto dto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  List<LogRequestStatusDto> getListStatusLogRequest();

//  String exportLog(Integer appId, Long fromDate, Long toDate, String status, String search)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException,
//      ParseException, IOException;

  void addService(Integer clientId, ClientServiceAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void pendingService(Integer clientId, Integer serviceId, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void deleteService(Integer clientId, Integer serviceId, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<CustomServiceDto> getServices(Integer clientId) throws ResourceNotFoundException;

  CustomServiceDto getService(Integer clientId, Integer serviceId) throws ResourceNotFoundException;

//  LogRequestResponseDto getLogRequestById(Long id)
//      throws ResourceNotFoundException, IdentifyBlankException;

  ServiceStatus getStatusOfService(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void addApi(Integer clientId, ClientApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  void deleteApi(Integer clientId, Long api, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<ApiClientServiceDto> getApis(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException;

  DataPagingResponse<ServiceResponseDto> getListServiceNotSettingOnApp(
      Integer appId, String search, String systems, String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException;

  List<UserAppPermission> getAllUserAppPermission(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  String createApiKey(Integer clientId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<ClientAuthTypeDto> getListClientAuthType();

  DataPagingResponse<ClientApiKeyResponseDto> getListClientApiKeyForApplication(
      Integer appId, String sort, Integer page, Integer limit)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  void deleteApiKey(Integer id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void cancelApiKey(String id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

}
