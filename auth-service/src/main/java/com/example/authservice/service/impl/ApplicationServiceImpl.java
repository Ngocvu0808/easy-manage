package com.example.authservice.service.impl;

import com.example.authservice.config.PermissionObjectCode;
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
import com.example.authservice.entities.SysPermission;
import com.example.authservice.entities.UserStatus;
import com.example.authservice.entities.application.*;
import com.example.authservice.entities.enums.*;
import com.example.authservice.entities.role.Role;
import com.example.authservice.entities.role.RoleDetail;
import com.example.authservice.entities.role.RoleObject;
import com.example.authservice.entities.service.ExternalApi;
import com.example.authservice.entities.user.User;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.filter.ClientApiFilter;
import com.example.authservice.filter.ClientApiKeyFilter;
import com.example.authservice.filter.ClientUserFilter;
import com.example.authservice.filter.ServiceFilter;
import com.example.authservice.mapper.*;
import com.example.authservice.repo.*;
import com.example.authservice.service.iface.ApplicationService;
import com.example.authservice.utils.*;
import com.example.authservice.utils.cache.CacheRedisService;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ApplicationService.class);

  private final ClientRepository clientRepository;
  private final ApiKeyRepository apiKeyRepository;
  private final UserRepository userRepository;
  private final ClientUserRepository clientUserRepository;
  private final ClientUserPermissionRepository clientUserPermissionRepository;
  private final RoleRepository roleRepository;
  private final RoleDetailRepositoryExtended roleDetailRepository;
  private final AccessTokenRepository accessTokenRepository;
  private final ClientServiceRepository clientServiceRepository;
  private final ServiceRepository serviceRepository;
  private final ExternalApiRepository externalApiRepository;
  private final ClientApiRepository clientApiRepository;
  private final ApiRequestRepository apiRequestRepository;
  private final RoleObjectRepository roleObjectRepository;
  private final ClientApiKeyRepository clientApiKeyRepository;


  private final ClientApiKeyMapper clientApiKeyMapper;
  private final UserClientMapper userClientMapper;
  private final RefreshTokenMapper refreshTokenMapper;
  private final AccessTokenMapper accessTokenMapper;
  private final ServiceMapper serviceMapper;
  private final ExternalApiMapper externalApiMapper;
  private final RoleMapper roleMapper;
  private final CacheRedisService redisService;
  private final ClientApiKeyFilter clientApiKeyFilter;


  @Value("${request-log.template.config-file-name:template_log.json}")
  private String configTemplateRequestLogFileName;

  public ApplicationServiceImpl(ClientRepository clientRepository,
                                ApiKeyRepository apiKeyRepository,
                                UserRepository userRepository,
                                ClientUserRepository clientUserRepository,
                                ClientUserPermissionRepository clientUserPermissionRepository,
                                RoleRepository roleRepository,
                                RoleDetailRepositoryExtended roleDetailRepository,
                                AccessTokenRepository accessTokenRepository,
                                ClientServiceRepository clientServiceRepository,
                                ServiceRepository serviceRepository,
                                ExternalApiRepository externalApiRepository,
                                ClientApiRepository clientApiRepository,
                                ApiRequestRepository apiRequestRepository,
                                RoleObjectRepository roleObjectRepository,
                                ClientApiKeyRepository clientApiKeyRepository,
                                ClientApiKeyMapper clientApiKeyMapper,
                                UserClientMapper userClientMapper,
                                RefreshTokenMapper refreshTokenMapper,
                                AccessTokenMapper accessTokenMapper,
                                ServiceMapper serviceMapper,
                                ExternalApiMapper externalApiMapper,
                                RoleMapper roleMapper, CacheRedisService redisService, ClientApiKeyFilter clientApiKeyFilter) {
    this.clientRepository = clientRepository;
    this.apiKeyRepository = apiKeyRepository;
    this.userRepository = userRepository;
    this.clientUserRepository = clientUserRepository;
    this.clientUserPermissionRepository = clientUserPermissionRepository;
    this.roleRepository = roleRepository;
    this.roleDetailRepository = roleDetailRepository;
    this.accessTokenRepository = accessTokenRepository;
    this.clientServiceRepository = clientServiceRepository;
    this.serviceRepository = serviceRepository;
    this.externalApiRepository = externalApiRepository;
    this.clientApiRepository = clientApiRepository;
    this.apiRequestRepository = apiRequestRepository;
    this.roleObjectRepository = roleObjectRepository;
    this.clientApiKeyRepository = clientApiKeyRepository;
    this.clientApiKeyMapper = clientApiKeyMapper;
    this.userClientMapper = userClientMapper;
    this.refreshTokenMapper = refreshTokenMapper;
    this.accessTokenMapper = accessTokenMapper;
    this.serviceMapper = serviceMapper;
    this.externalApiMapper = externalApiMapper;
    this.roleMapper = roleMapper;
    this.redisService = redisService;
    this.clientApiKeyFilter = clientApiKeyFilter;
  }


  private static final String TYPE_ACCESS_TOKEN = "access-token";
  public static final String REQUEST_LOG_NAME = "request_log_name";
  public static final String SUFFIX = ".csv";
  private static final Long LONG_TIME_A_DAY = 86400000L;

  @Value("${request-log.export.dir:export/}")
  private String PATH_FOLDER_EXPORT_REQUEST_LOG;

  @Override
  public List<RefreshTokenResponseDto> getRefreshTokensByClientId(RefreshTokenStatus status,
                                                                  Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("App id null",
          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
    }
    Client clientOptional = clientRepository.findByIdAndIsDeletedFalse(id);
    if (clientOptional == null) {
      throw new OperationNotImplementException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (!ClientAuthType.OAUTH.equals(clientOptional.getAuthType())) {
      throw new ResourceNotFoundException("client auth type invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_AUTH_TYPE_INVALID);
    }

    List<RefreshToken> refreshTokens = clientOptional.getRefreshTokens().stream()
        .filter(it -> it.getDeleted().equals(Boolean.FALSE))
        .collect(Collectors.toList());
    List<RefreshTokenResponseDto> refreshTokenResponseDtos = new ArrayList<>();

    if (status == null) {
      refreshTokens.forEach(refreshToken -> refreshTokenResponseDtos
          .add(refreshTokenMapper.toRefreshTokenResponseDto(refreshToken)));
    } else {
      refreshTokens.forEach(refreshToken -> {
        RefreshTokenResponseDto refreshTokenResponseDto = refreshTokenMapper
            .toRefreshTokenResponseDto(refreshToken);
        if (refreshToken.getExpireTime() <= System.currentTimeMillis()) {
          refreshTokenResponseDto.setStatus(RefreshTokenStatus.EXPIRED);
        }
        if (refreshTokenResponseDto.getStatus().equals(status)) {
          refreshTokenResponseDtos.add(refreshTokenResponseDto);
        }
      });
    }
    return refreshTokenResponseDtos;
  }

  @Override
  public List<AccessTokenResponseDto> getAccessTokenByClientId(TokenStatus status, Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("App id null",
          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
    }
    List<AccessTokenResponseDto> accessTokenResponseDtos = new ArrayList<>();
    Client clientById = clientRepository.findByIdAndIsDeletedFalse(id);
    if (clientById == null) {
      throw new OperationNotImplementException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (!ClientAuthType.OAUTH.equals(clientById.getAuthType())) {
      throw new ResourceNotFoundException("client auth type invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_AUTH_TYPE_INVALID);
    }
    clientById.getRefreshTokens().stream().filter(refreshToken -> !refreshToken.getIsDeleted())
        .forEach(refreshToken -> {
          List<AccessToken> accessTokens = refreshToken.getAccessTokens();
          for (AccessToken accessToken : accessTokens) {
            if (!accessToken.getIsDeleted()) {
              AccessTokenResponseDto accessTokenResponseDto = accessTokenMapper
                  .toAccessTokenResponseDto(accessToken);
              if (accessToken.getExpireTime() <= System.currentTimeMillis()) {
                accessTokenResponseDto.setStatus(TokenStatus.EXPIRED);
              }
              if (status == null) {
                accessTokenResponseDtos.add(accessTokenResponseDto);
              } else {
                if (accessTokenResponseDto.getStatus().equals(status)) {
                  accessTokenResponseDtos.add(accessTokenResponseDto);
                }
              }
            }
          }
        });
    return accessTokenResponseDtos;
  }

  @Override
  @Transactional
  public void addUser(Integer clientId, Integer creatorUserId, ClientUserAddRequestDto requestDto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<User> userOptional = userRepository.findById(requestDto.getUserId());
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    if (requestDto.getRoleIds() == null || requestDto.getRoleIds().isEmpty()) {
      throw new IdentifyBlankException("roles not null",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_LIST_NOT_NULL);
    }
    Optional<ClientUser> clientUserOptional = clientUserRepository
        .findByClientIdAndUserIdAndIsDeletedFalse(clientId, requestDto.getUserId());
    if (clientUserOptional.isEmpty()) {
      ClientUser clientUser = new ClientUser();
      clientUser.setClient(clientOptional.get());
      clientUser.setUser(userOptional.get());
      clientUser.setCreatorUserId(creatorUserId);
      clientUser.setDeleted(Boolean.FALSE);
      clientUserRepository.save(clientUser);
      addRoleUser(clientId, creatorUserId, requestDto);
    }

  }

  void addRoleUser(Integer clientId, Integer creatorUserId, ClientUserAddRequestDto requestDto)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<ClientUser> clientUserOptional = clientUserRepository
        .findByClientIdAndUserIdAndIsDeletedFalse(clientId, requestDto.getUserId());
    if (clientUserOptional.isEmpty()) {
      throw new ResourceNotFoundException("user does not exist, please add user to application",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST_IN_APP);
    }
    ClientUser clientUser = clientUserOptional.get();
    Integer clientUserId = clientUser.getId();
    List<ClientUserPermission> listRoleActive = clientUserPermissionRepository
        .findAllByClientUserIdAndIsDeletedFalse(clientUserId);
    List<Integer> listIdActive = listRoleActive.stream()
        .map(ClientUserPermission::getId)
        .collect(Collectors.toList());
    List<Integer> listIdAdd = requestDto.getRoleIds().stream().distinct()
        .filter(it -> !listIdActive.contains(it)).collect(Collectors.toList());
    List<Integer> listIdRemove = listIdActive.stream().distinct()
        .filter(it -> !requestDto.getRoleIds().contains(it)).collect(Collectors.toList());
    if (!listIdRemove.isEmpty()) {
      List<ClientUserPermission> list = clientUserPermissionRepository
          .findByClientUserIdAndRoleIdInAndIsDeletedFalse(clientUserId, listIdRemove);
      list.forEach(it -> {
        it.setDeleted(Boolean.TRUE);
        it.setDeleterUserId(creatorUserId);
      });
      clientUserPermissionRepository.saveAll(list);
      removeRoleObject(clientUser.getClientId(), creatorUserId, clientUser.getUserId(),
          listIdRemove);
    }
    // getListRoleOfClient
    List<Integer> roleClientIdList = roleDetailRepository
        .findRoleOnlyContainsPermissionObject(
            Collections.singletonList(PermissionObjectCode.APPLICATION));
    // add role
    if (!listIdAdd.isEmpty()) {
      List<ClientUserPermission> roleList = new ArrayList<>();
      for (Integer roleId : listIdAdd) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty() || roleOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
          throw new ResourceNotFoundException("Role does not exist: " + roleId,
              ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
        }
        if (!roleClientIdList.contains(roleId)) {
          throw new OperationNotImplementException("role invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.ROLE_INVALID);
        }
        Optional<ClientUserPermission> optional = clientUserPermissionRepository
            .findByClientUserIdAndRoleIdAndIsDeletedFalse(clientUserId, roleId);
        if (optional.isPresent()) {
          continue;
        }
        ClientUserPermission clientUserPermission = new ClientUserPermission();
        clientUserPermission.setClientUser(clientUser);
        clientUserPermission.setRole(roleOptional.get());
        clientUserPermission.setCreatorUserId(creatorUserId);
        clientUserPermission.setDeleted(Boolean.FALSE);
        roleList.add(clientUserPermission);
        addRoleObject(clientId, creatorUserId, clientUser.getUser(), roleOptional.get());
      }
      clientUserPermissionRepository.saveAll(roleList);
    }
  }

  void addRoleObject(Integer objectId, Integer creatorUserId, User user, Role role) {
    Optional<RoleObject> optional = roleObjectRepository
        .findByServiceNameAndObjectIdAndUserIdAndRoleIdAndIsDeletedFalse(
            PermissionObjectCode.APPLICATION, objectId, user.getId(), role.getId());
    if (optional.isEmpty()) {
      RoleObject roleObject = new RoleObject();
      roleObject.setServiceName(PermissionObjectCode.APPLICATION);
      roleObject.setObjectId(objectId);
      roleObject.setUser(user);
      roleObject.setRole(role);
      roleObject.setCreatorUserId(creatorUserId);
      roleObject.setIsDeleted(Boolean.FALSE);
      roleObjectRepository.save(roleObject);
    }
  }

  void removeRoleObject(Integer objectId, Integer updaterUserId, Integer userId,
      List<Integer> roleIds) {
    List<RoleObject> roleObjectList = roleObjectRepository
        .findAllByServiceNameAndObjectIdAndUserIdAndRoleIdInAndIsDeletedFalse(
            PermissionObjectCode.APPLICATION, objectId, userId, roleIds);
    roleObjectList.forEach(it -> {
      it.setIsDeleted(Boolean.TRUE);
      it.setUpdaterUserId(updaterUserId);
    });
    roleObjectRepository.saveAll(roleObjectList);
  }

  @Override
  public List<UserClientCustomDto> getUsers(Integer clientId, String roles, String search,
                                            String sort) throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Set<Integer> roleList = Utils.strToIntegerSet(roles);
    List<ClientUser> clientUsers = clientUserRepository
        .findAll(new ClientUserFilter().filter(clientId, roleList, search, sortMap, false));
    List<UserClientCustomDto> list = clientUsers.stream()
        .filter(it -> it.getDeleted().equals(Boolean.FALSE)
            && it.getUser().getIsDeleted().equals(Boolean.FALSE)
            && it.getUser().getStatus().equals(UserStatus.ACTIVE))
        .map(userClientMapper::toDto)
        .collect(Collectors.toList());
    for (UserClientCustomDto it : list) {
      List<ClientUserPermissionDto> permissionList = it.getPermissions().stream()
          .filter(Utils.distinctByKey(ClientUserPermissionDto::getRoleId))
          .collect(Collectors.toList());
      it.setPermissions(permissionList);
    }
    return list;
  }

  @Override
  public void deleteUser(Integer clientId, Integer userId, Integer deleterUserId)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ClientUser> clientUserOptional = clientUserRepository
        .findByClientIdAndUserIdAndIsDeletedFalse(clientId, userId);
    if (clientUserOptional.isEmpty()) {
      throw new ResourceNotFoundException("user does not exist in application",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }
    ClientUser clientUser = clientUserOptional.get();
    clientUser.setDeleted(Boolean.TRUE);
    clientUser.setDeleterUserId(deleterUserId);
    clientUserRepository.save(clientUser);
    List<ClientUserPermission> permissions = clientUserPermissionRepository
        .findAllByClientUserIdAndIsDeletedFalse(clientUser.getId());
    if (!permissions.isEmpty()) {
      permissions.forEach(it -> {
        it.setDeleted(Boolean.TRUE);
        it.setDeleterUserId(deleterUserId);
      });
      clientUserPermissionRepository.saveAll(permissions);
    }
  }

  @Override
  public void updateRoleUser(Integer clientId, Integer updaterUserId, ClientUserAddRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<User> userOptional = userRepository.findById(dto.getUserId());
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    this.addRoleUser(clientId, updaterUserId, dto);
  }

  @Override
  public List<RoleCustomDto> getListRoleAssigned(Integer clientId)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    List<Integer> clientUserIds = clientUserRepository.findAllIdByClientId(clientId);
    List<ClientUserPermission> permissions = clientUserPermissionRepository
        .findAllByClientUserIdInAndIsDeletedFalse(clientUserIds);
    Set<Role> roleList = permissions.stream()
        .map(ClientUserPermission::getRole)
        .filter(it -> !it.getIsDeleted())
        .collect(Collectors.toSet());
    return roleList.stream().map(roleMapper::toRoleCustomerDto).collect(Collectors.toList());
  }

  @Override
  public void updateStatusOfAccessToken(AccessTokenStatusRequestDto dto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {
    if (dto == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (dto.getId() == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_ACCESS_TOKEN_NULL);
    }
    if (dto.getStatus() == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    AccessToken accessTokenById = accessTokenRepository
        .findByIdAndIsDeletedFalse(dto.getId());
    if (accessTokenById == null) {
      throw new ResourceNotFoundException("access token not found",
          ServiceInfo.getId() + AuthServiceMessageCode.ACCESS_TOKEN_NOT_FOUND);
    }
    if (accessTokenById.getStatus().equals(TokenStatus.EXPIRED) || accessTokenById.getStatus()
        .equals(TokenStatus.REJECTED)) {
      throw new OperationNotImplementException("AccessToken expired or rejected",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (accessTokenById.getStatus().equals(TokenStatus.ACTIVE) && dto
        .getStatus().equals(TokenStatus.ACTIVE)) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (accessTokenById.getStatus().equals(TokenStatus.DEACTIVE) && dto
        .getStatus().equals(TokenStatus.DEACTIVE)) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    accessTokenById.setStatus(dto.getStatus());
    User user = checkValidUser(userId);
    accessTokenById.setUpdaterUser(user);
    accessTokenRepository.save(accessTokenById);
    if (dto.getStatus().equals(TokenStatus.REJECTED)) {
      // remove key redis
      String redisKey = KeyConstants.RedisKey.AUTH_TOKEN.concat(accessTokenById.getToken()).trim();
      if (redisService.exists(redisKey)) {
        redisService.remove(redisKey);
      }
    }
  }

  public User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepository.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("user not found",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_DEACTIVE);
    }
    return userOptionalById.get();
  }

  @Override
  @Transactional
  public void addService(Integer clientId, ClientServiceAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException {
    this.checkValidUser(creatorId);
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(dto.getServiceId());
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    Optional<ClientService> optionalClientService = clientServiceRepository
        .findByClientIdAndServiceIdAndIsDeletedFalse(clientId, dto.getServiceId());
    if (optionalClientService.isPresent()) {
      throw new OperationNotImplementException("service already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_ALREADY_EXIST);
    }
    ClientService clientService = new ClientService();
    clientService.setClient(clientOptional.get());
    clientService.setService(serviceOptional.get());
    clientService.setStatus(ClientServiceStatus.ACTIVE);
    clientService.setDeleted(Boolean.FALSE);
    clientService.setCreatorUserId(creatorId);

    clientServiceRepository.save(clientService);
  }

  @Override
  @Transactional
  public void pendingService(Integer clientId, Integer serviceId, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    this.checkValidUser(updaterId);
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(serviceId);
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    Optional<ClientService> optionalClientService = clientServiceRepository
        .findByClientIdAndServiceIdAndIsDeletedFalse(clientId, serviceId);
    if (optionalClientService.isEmpty()) {
      throw new OperationNotImplementException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }

    ClientService clientService = optionalClientService.get();
    clientService.setStatus(ClientServiceStatus.DEACTIVE);
    clientService.setUpdaterUserId(updaterId);
    clientService.setDeleterUserId(updaterId);
    clientService.setDeleted(Boolean.TRUE);

    clientServiceRepository.save(clientService);
    // remove all api of service
    List<ClientApi> clientApiList = clientApiRepository
        .findAllByClientIdAndServiceId(clientId, serviceId);
    if (clientApiList.size() > 0) {
      clientApiList.forEach(it -> {
        it.setDeleted(Boolean.TRUE);
        it.setDeleterUserId(updaterId);
      });
      clientApiRepository.saveAll(clientApiList);
    }
    // reload JWT of api_key
    this.reloadJwtOfApiKey(clientId);
  }

  @Override
  public List<LogRequestStatusDto> getListStatusLogRequest() {
    List<LogRequestStatusDto> filterList = new ArrayList<>();
    HttpStatusCustom[] statusList = HttpStatusCustom.class.getEnumConstants();
    for (HttpStatusCustom status : statusList) {
      LogRequestStatusDto filter = new LogRequestStatusDto();
      filter.setName(status.name());
      filter.setValue(status.getValue());
      filter.setDescription(status.getDescription());
      filterList.add(filter);
    }
    return filterList;
  }
//
//
//
//  @Override
//  public LogRequestResponseDto getLogRequestById(Long id)
//          throws ResourceNotFoundException, IdentifyBlankException {
//
//    if (id == null) {
//      throw new IdentifyBlankException("id request log null",
//              ServiceInfo.getId() + AuthServiceMessageCode.ID_REQUEST_LOG_NULL);
//    }
//
//    Optional<LogRequest> optionalLogRequestById = logRequestRepository.findById(id);
//
//    if (optionalLogRequestById.isEmpty()) {
//      throw new ResourceNotFoundException("log request not found",
//              ServiceInfo.getId() + AuthServiceMessageCode.ID_REQUEST_LOG_NULL);
//    }
//
//    LogRequest logRequest = optionalLogRequestById.get();
//    if (!logRequest.getAuthType().equals(TYPE_ACCESS_TOKEN)) {
//      throw new ResourceNotFoundException("log request invalid",
//              ServiceInfo.getId() + AuthServiceMessageCode.LOG_REQUEST_INVALID);
//    }
//
//    AccessToken accessTokenById = accessTokenRepository
//            .findByTokenAndIsDeletedFalse(logRequest.getToken());
//
//    String nameUser = null;
//    if (accessTokenById != null) {
//      RefreshToken refreshToken = accessTokenById.getRefreshToken();
//      if (refreshToken != null && !refreshToken.getIsDeleted()) {
//        Integer apiKeyId = refreshToken.getApiKeyId();
//        if (apiKeyId != null) {
//          ApiKey apiKeyById = apiKeyRepository.findByIdAndIsDeletedFalse(apiKeyId);
//          if (apiKeyById != null) {
//            User user = apiKeyById.getUser();
//            if (user != null && !user.getIsDeleted()) {
//              nameUser = user.getUsername();
//            }
//          }
//
//        }
//      }
//    }
//
//    LogRequestResponseDto logResponseDto = logRequestMapper.toLogResponseDto(logRequest);
//    logResponseDto.setNameUser(nameUser);
//    return logResponseDto;
//  }
//  @Override
//  public DataRequestLogResponse<?> getLogRequestByAppId(Integer appId, String search, Long fromDate,
//      Long toDate, String status, String sort, Integer page, Integer limit)
//      throws ResourceNotFoundException, IdentifyBlankException, ParseException, OperationNotImplementException {
//
//    if (appId == null) {
//      throw new IdentifyBlankException("Id null",
//          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
//    }
//    checkValidFromDateAndToDate(fromDate, toDate);
//    Client clientById = clientRepository.findByIdAndIsDeletedFalse(appId);
//    if (clientById == null) {
//      throw new ResourceNotFoundException("application not found",
//          ServiceInfo.getId() + AuthServiceMessageCode.APP_NOT_FOUND);
//    }
//
//    List<LogRequestResponseDto> logsResponse = new ArrayList<>();
//    Set<String> listTokenNeedFilter = new HashSet<>();
//    List<String> listApiKeyNeedFilter = new ArrayList<>();
//    DataRequestLogResponse<LogRequestResponseDto> dataRequestLogResponse = new DataRequestLogResponse<>();
//
//    List<Integer> statusFilter = new ArrayList<>();
//    String[] statusList = status.split(",");
//    if (statusList.length > 0) {
//      for (String st : statusList) {
//        try {
//          HttpStatusCustom httpStatusCustom = HttpStatusCustom.valueOf(st.trim().toUpperCase());
//          statusFilter.add(httpStatusCustom.getValue());
//        } catch (Exception ignored) {
//        }
//      }
//
//    }
//    Map<String, String> map = SortingUtils.detectSortType(sort);
//
//    Page<LogRequest> logRequestLogPages;
//    Specification<LogRequest> filter = new RequestLogClientFilter()
//        .getByFilter(search, fromDate, toDate, listTokenNeedFilter,
//            listApiKeyNeedFilter, clientById.getAuthType(), statusFilter, map);
//    PageRequest pageRequest = PageRequest.of(page - 1, limit);
//    // filter by oauth
//    if (clientById.getAuthType().equals(ClientAuthType.OAUTH)) {
//      List<RefreshToken> refreshTokenList = clientById.getRefreshTokens();
//      refreshTokenList.forEach(token -> {
//        List<AccessToken> accessTokens = token.getAccessTokens();
//        for (AccessToken accessToken : accessTokens) {
//          if (accessToken.getToken() != null && !accessToken.getToken().isEmpty()) {
//            listTokenNeedFilter.add(accessToken.getToken());
//          }
//        }
//      });
//
//      logRequestLogPages = logRequestRepository.findAll(filter, pageRequest);
//      setUserUseRequestLog(logRequestLogPages.getContent(), logsResponse);
//    } else {
//
//      List<ClientApiKey> listClientApiKey = clientById.getClientApiKey();
//      logger.info("list api_key size: {}", listClientApiKey.size());
//      for (ClientApiKey clientApiKey : listClientApiKey) {
//        if (clientApiKey.getApiKey() != null && !clientApiKey.getApiKey().isEmpty()) {
//          listApiKeyNeedFilter.add(clientApiKey.getApiKey());
//        }
//      }
//
//      logRequestLogPages = logRequestRepository.findAll(filter, pageRequest);
//
//      logsResponse = logRequestLogPages.getContent().stream()
//          .map(logRequestMapper::toLogResponseDto).collect(Collectors.toList());
//      this.hideAccessToken(logsResponse);
//      User owner = clientById.getOwner();
//      if (owner != null && owner.getStatus().equals(UserStatus.ACTIVE)) {
//        logsResponse.forEach(it -> it.setNameUser(owner.getUsername()));
//      }
//    }
//    List<TemplateLogRequest> temples = getTemplates();
//    List<String> fieldNames = temples.stream().map(TemplateLogRequest::getKey)
//        .collect(Collectors.toList());
//    this.convertData(fieldNames, logsResponse);
//
//    dataRequestLogResponse.setList(logsResponse);
//    dataRequestLogResponse.setCurrentPage(page);
//    dataRequestLogResponse.setNum(logRequestLogPages.getTotalElements());
//    dataRequestLogResponse.setTotalPage(logRequestLogPages.getTotalPages());
//    dataRequestLogResponse.setTemplate(getTemplates());
//    return dataRequestLogResponse;
//  }
//
//  List<LogRequestResponseDto> getLogRequestExport(Integer appId, String search, Long fromDate,
//      Long toDate, String status)
//      throws IdentifyBlankException, ResourceNotFoundException, ParseException, OperationNotImplementException {
//    if (appId == null) {
//      throw new IdentifyBlankException("Id null",
//          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
//    }
//    checkValidFromDateAndToDate(fromDate, toDate);
//    Client clientById = clientRepository.findByIdAndIsDeletedFalse(appId);
//    if (clientById == null) {
//      throw new ResourceNotFoundException("application not found",
//          ServiceInfo.getId() + AuthServiceMessageCode.APP_NOT_FOUND);
//    }
//    List<LogRequestResponseDto> logsResponse = new ArrayList<>();
//    Set<String> listTokenNeedFilter = new HashSet<>();
//    List<String> listApiKeyNeedFilter = new ArrayList<>();
//    List<Integer> statusFilter = new ArrayList<>();
//    String[] statusList = status.split(",");
//    if (statusList.length > 0) {
//      for (String st : statusList) {
//        try {
//          HttpStatusCustom httpStatusCustom = HttpStatusCustom.valueOf(st.trim().toUpperCase());
//          statusFilter.add(httpStatusCustom.getValue());
//        } catch (Exception ignored) {
//        }
//      }
//
//    }
//    Specification<LogRequest> filter = new RequestLogClientFilter()
//        .getByFilter(search, fromDate, toDate, listTokenNeedFilter,
//            listApiKeyNeedFilter, clientById.getAuthType(), statusFilter, null);
//    // filter by oauth
//    if (clientById.getAuthType().equals(ClientAuthType.OAUTH)) {
//      List<RefreshToken> refreshTokenList = clientById.getRefreshTokens();
//      refreshTokenList.forEach(token -> {
//        List<AccessToken> accessTokens = token.getAccessTokens();
//        for (AccessToken accessToken : accessTokens) {
//          if (accessToken.getToken() != null && !accessToken.getToken().isEmpty()) {
//            listTokenNeedFilter.add(accessToken.getToken());
//          }
//        }
//      });
//
//      List<LogRequest> logRequests = logRequestRepository.findAll(filter);
//      setUserUseRequestLog(logRequests, logsResponse);
//    } else {
//
//      List<ClientApiKey> listClientApiKey = clientById.getClientApiKey();
//      logger.info("list api_key size: {}", listClientApiKey.size());
//      for (ClientApiKey clientApiKey : listClientApiKey) {
//        if (clientApiKey.getApiKey() != null && !clientApiKey.getApiKey().isEmpty()) {
//          listApiKeyNeedFilter.add(clientApiKey.getApiKey());
//        }
//      }
//
//      List<LogRequest> logRequests = logRequestRepository.findAll(filter);
//
//      logsResponse = logRequests.stream()
//          .map(logRequestMapper::toLogResponseDto).collect(Collectors.toList());
//      User owner = clientById.getOwner();
//      if (owner != null && owner.getStatus().equals(UserStatus.ACTIVE)) {
//        logsResponse.forEach(it -> it.setNameUser(owner.getUsername()));
//      }
//    }
//
//    return logsResponse;
//  }
//
//  void hideAccessToken(List<LogRequestResponseDto> logs) {
//    logs.forEach(it -> {
//      if (it.getToken() != null && !it.getToken().isEmpty()) {
//        it.setToken(it.getToken().substring(0, 15) + "...");
//      }
//    });
//  }
//
//  @Override
//  public String exportLog(Integer appId, Long fromDate, Long toDate, String status, String search)
//      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException, ParseException, IOException {
//    List<LogRequestResponseDto> listRequestLogExport = this
//        .getLogRequestExport(appId, search, fromDate,
//            toDate, status);
//    List<TemplateLogRequest> templates = this.getTemplates();
//    String[] keyFromTemplates = getKeyFromTemplate(templates);
//    String[] nameFromTemplates = getNameFromTemplate(templates);
//
//    //create file
//    String fileName = PATH_FOLDER_EXPORT_REQUEST_LOG.concat(REQUEST_LOG_NAME).concat("_")
//        .concat(UUID.randomUUID().toString()).concat(SUFFIX);
//    if (!Files.exists(Paths.get(PATH_FOLDER_EXPORT_REQUEST_LOG))) {
//      File file = new File(PATH_FOLDER_EXPORT_REQUEST_LOG);
//      file.mkdir();
//    }
//
//    //set name for header file csv
//    CsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(fileName),
//        CsvPreference.STANDARD_PREFERENCE);
//    csvBeanWriter.writeHeader(nameFromTemplates);
//
//    //write content
//    for (LogRequestResponseDto log : listRequestLogExport) {
//      csvBeanWriter.write(log, keyFromTemplates);
//    }
//    csvBeanWriter.close();
//    return fileName;
//  }
//
//  public void setUserUseRequestLog(List<LogRequest> logRequests, List<LogRequestResponseDto> res) {
//    for (LogRequest logRequest : logRequests) {
//      LogRequestResponseDto logRequestResponseDto = logRequestMapper.toLogResponseDto(logRequest);
//      String token = logRequest.getToken();
//      if (token != null && !token.isEmpty()) {
//        List<AccessToken> accessTokens = accessTokenRepository.findByToken(token);
//        if (accessTokens != null) {
//          Set<RefreshToken> refreshTokens = accessTokens.stream()
//              .map(AccessToken::getRefreshToken)
//              .collect(Collectors.toSet());
//
//          Set<Integer> listApiKeyUser = refreshTokens.stream()
//              .map(RefreshToken::getApiKeyId).collect(Collectors.toSet());
//
//          logger.info("listApiKeyUser: {}", listApiKeyUser);
//
//          for (Integer apiKeyUser : listApiKeyUser) {
//            ApiKey apikey = apiKeyRepository
//                .findByIdAndIsDeletedFalse(apiKeyUser);
//            if (apikey != null) {
//              User user = apikey.getUser();
//              if (user != null) {
//                logRequestResponseDto.setNameUser(user.getUsername());
//              }
//            }
//
//          }
//        }
//      }
//      res.add(logRequestResponseDto);
//    }
//  }
//
//
//  public List<TemplateLogRequest> getTemplates() {
//    try {
//      String data = Utils.readFileAsString(configTemplateRequestLogFileName);
//      return Arrays.asList(new Gson().fromJson(data, TemplateLogRequest[].class));
//    } catch (Exception e) {
//      logger.info("error when read template config, reason: {}", e.getMessage());
//      return new ArrayList<>();
//    }
//
//  }
//
//  public String[] getKeyFromTemplate(List<TemplateLogRequest> templates) {
//    List<String> values = new ArrayList<>();
//    templates.forEach(template -> values.add(template.getKey()));
//    return values.toArray(String[]::new);
//  }
//
//  public String[] getNameFromTemplate(List<TemplateLogRequest> templates) {
//    List<String> values = new ArrayList<>();
//    templates.forEach(template -> values.add(template.getName()));
//    return values.toArray(String[]::new);
//  }

  void convertData(List<String> fieldNames, List<LogRequestResponseDto> data) {
    Field[] fields = LogRequestResponseDto.class.getDeclaredFields();
    for (Field f : fields) {
      if (!fieldNames.contains(f.getName())) {
        for (LogRequestResponseDto log : data) {
          try {
            f.setAccessible(true);
            f.set(log, null);
          } catch (Exception ignored) {
          }
        }
      }
    }
  }

  public void checkValidFromDateAndToDate(Long fromDateTimeStamp, Long toDateTimeStamp)
      throws OperationNotImplementException, ParseException {
    Long onlyDateFromTimeStamp = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    if (fromDateTimeStamp != null & toDateTimeStamp != null) {
      if (fromDateTimeStamp > toDateTimeStamp) {
        throw new OperationNotImplementException("fromDate greater toDate",
            ServiceInfo.getId() + AuthServiceMessageCode.FROM_DATE_GREATER_TO_DATE);
      }
      if (fromDateTimeStamp > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("fromDate greater currentDate",
            ServiceInfo.getId() + AuthServiceMessageCode.FROM_DATE_GREATER_CURRENT_DATE);
      }
      if (toDateTimeStamp > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("toDate greater currentDate",
            ServiceInfo.getId() + AuthServiceMessageCode.TO_DATE_GREATER_CURRENT_DATE);
      }
    }
  }

  @Override
  public void deleteService(Integer clientId, Integer serviceId, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    this.checkValidUser(deleterId);
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(serviceId);
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    Optional<ClientService> optionalClientService = clientServiceRepository
        .findByClientIdAndServiceIdAndIsDeletedFalse(clientId, serviceId);
    if (optionalClientService.isEmpty()) {
      throw new OperationNotImplementException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    ClientService clientService = optionalClientService.get();
    clientService.setDeleted(Boolean.TRUE);
    clientService.setDeleterUserId(deleterId);

    clientServiceRepository.save(clientService);
    // remove all api of service
    List<ClientApi> clientApiList = clientApiRepository
        .findAllByClientIdAndServiceId(clientId, serviceId);
    if (clientApiList.size() > 0) {
      clientApiList.forEach(it -> {
        it.setDeleted(Boolean.TRUE);
        it.setDeleterUserId(deleterId);
      });
      clientApiRepository.saveAll(clientApiList);
    }
    // reload JWT of api_key
    this.reloadJwtOfApiKey(clientId);
  }

  @Override
  public List<CustomServiceDto> getServices(Integer clientId) throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    List<ClientService> clientServiceList = clientServiceRepository
        .findAllByClientIdAndIsDeletedFalse(clientId);

    List<com.example.authservice.entities.service.Service> serviceList = clientServiceList.stream()
        .filter(it -> !it.getDeleted() && !it.getService().getIsDeleted())
        .map(ClientService::getService).collect(Collectors.toList());
    serviceList.forEach(it -> {
      Optional<ClientService> clientServiceOptional = clientServiceList.stream().
          filter(p -> p.getService().getId().equals(it.getId())).findFirst();
      if (clientServiceOptional.isPresent()) {
        ClientService clientApi = clientServiceOptional.get();
        if (it.getStatus().equals(ServiceStatus.ACTIVE)) {
          it.setStatus(ServiceStatus.valueOf(clientApi.getStatus().name()));
        }
      }
    });
    return serviceList.stream()
        .map(serviceMapper::toCustomDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomServiceDto getService(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }

    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(serviceId);
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    com.example.authservice.entities.service.Service service = serviceOptional.get();

    Optional<ClientService> clientServiceOptional = clientServiceRepository
        .findByClientIdAndServiceIdAndIsDeletedFalse(clientId, serviceId);
    if (clientServiceOptional.isEmpty() || clientServiceOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    CustomServiceDto dto = serviceMapper.toCustomDto(service);
    if (dto.getStatus().equals(ServiceStatus.ACTIVE)) {
      dto.setStatus(ServiceStatus.valueOf(service.getStatus().name()));
    }
    return dto;
  }


  @Override
  public ServiceStatus getStatusOfService(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<ClientService> optionalClientService = clientServiceRepository
        .findByClientIdAndServiceIdAndIsDeletedFalse(clientId, serviceId);
    if (optionalClientService.isEmpty()) {
      throw new OperationNotImplementException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    ClientService clientService = optionalClientService.get();

    return clientService.getService().getStatus().equals(ServiceStatus.ACTIVE) ? ServiceStatus
        .valueOf(clientService.getStatus().name()) : ServiceStatus.DEACTIVE;
  }


  @Override
  public void addApi(Integer clientId, ClientApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    User user = this.checkValidUser(creatorId);
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (dto.getId() == null) {
      throw new IdentifyBlankException("id not null",
          ServiceInfo.getId() + AuthServiceMessageCode.API_ID_NOT_NULL);
    }
    if (dto.getPurpose() == null || dto.getPurpose().isBlank()) {
      throw new IdentifyBlankException("purpose not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PURPOSE_NOT_NULL);
    }
    Optional<ExternalApi> externalApiOptional = externalApiRepository.findById(dto.getId());
    if (externalApiOptional.isEmpty() || externalApiOptional.get().getIsDeleted()
        || externalApiOptional.get().getStatus().equals(ApiStatus.DEACTIVE)) {
      throw new ResourceNotFoundException("api invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.API_INVALID);
    }
    ExternalApi externalApi = externalApiOptional.get();
    com.example.authservice.entities.service.Service service = externalApi.getService();
    if (service.getIsDeleted().equals(Boolean.TRUE) ||
        service.getStatus().equals(ServiceStatus.DEACTIVE)) {
      throw new OperationNotImplementException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    Optional<ClientApi> clientApiOptional = clientApiRepository
        .findByClientIdAndApiIdAndIsDeletedFalse(clientId, dto.getId());
    if (clientApiOptional.isPresent()) {
      throw new OperationNotImplementException("api already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.API_ALREADY_EXIST);
    }

    ClientApi clientApi = new ClientApi();
    clientApi.setApi(externalApiOptional.get());
    clientApi.setClient(clientOptional.get());
    clientApi.setCreatorUserId(creatorId);
    clientApi.setStatus(ClientApiStatus.DEACTIVE);
    clientApi.setDeleted(Boolean.FALSE);

    clientApiRepository.save(clientApi);

    ApiRequest apiRequest = new ApiRequest();
    apiRequest.setApi(externalApiOptional.get());
    apiRequest.setClient(clientOptional.get());
    apiRequest.setDeleted(Boolean.FALSE);
    apiRequest.setStatus(ApiRequestStatus.REQUESTING);
    apiRequest.setPurpose(dto.getPurpose());
    apiRequest.setCreatorUser(user);

    apiRequestRepository.save(apiRequest);
  }

  @Override
  public void deleteApi(Integer clientId, Long apiId, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    this.checkValidUser(deleterId);
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Optional<ExternalApi> externalApiOptional = externalApiRepository.findById(apiId);
    if (externalApiOptional.isEmpty() || externalApiOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("api invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.API_INVALID);
    }

    Optional<ClientApi> clientApiOptional = clientApiRepository
        .findByClientIdAndApiIdAndIsDeletedFalse(clientId, apiId);
    if (clientApiOptional.isEmpty() || clientApiOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("api not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_NOT_FOUND);
    }
    ClientApi clientApi = clientApiOptional.get();
    clientApi.setDeleted(Boolean.TRUE);
    clientApi.setDeleterUserId(deleterId);
    clientApiRepository.save(clientApi);

    if (clientApi.getStatus().equals(ClientApiStatus.DEACTIVE)) {
      Optional<ApiRequest> apiRequestOptional = apiRequestRepository
          .findByClientIdAndApiIdAndIsDeletedFalse(clientId, apiId);
      if (apiRequestOptional.isPresent()) {
        ApiRequest apiRequest = apiRequestOptional.get();
        apiRequest.setDeleted(Boolean.TRUE);
        apiRequest.setDeleterUserId(deleterId);
        apiRequestRepository.save(apiRequest);
      }
    }
    // reload JWT of api_key
    this.reloadJwtOfApiKey(clientId);
  }

  @Override
  public List<ApiClientServiceDto> getApis(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    List<ClientApi> clientApiList = clientApiRepository
        .findAll(new ClientApiFilter().filterByClientIdAndServiceId(clientId, serviceId));
    List<ExternalApi> apis = clientApiList.stream()
        .map(ClientApi::getApi)
        .collect(Collectors.toList());
    List<ApiClientServiceDto> clientServiceApiList = apis.stream()
        .filter(it -> it.getStatus().equals(ApiStatus.ACTIVE))
        .map(externalApiMapper::toApiClientServiceDto)
        .collect(Collectors.toList());
    clientServiceApiList.forEach(it -> {
      Optional<ClientApi> clientApiOptional = clientApiList.stream().
          filter(p -> p.getApi().getId().equals(it.getId())).
          findFirst();
      if (clientApiOptional.isPresent()) {
        ClientApi clientApi = clientApiOptional.get();
        it.setStatus(ApiStatus.valueOf(clientApi.getStatus().name()));
      }
    });
    List<Long> idApiList = clientServiceApiList.stream()
        .map(ApiClientServiceDto::getId)
        .collect(Collectors.toList());

    List<ExternalApi> apiList = externalApiRepository
        .findAllByServiceIdAndIsDeletedFalse(serviceId);
    List<ApiClientServiceDto> serviceApiList = apiList.stream()
        .filter(it -> !idApiList.contains(it.getId()))
        .filter(it -> it.getStatus().equals(ApiStatus.ACTIVE))
        .map(externalApiMapper::toApiClientServiceDto)
        .collect(Collectors.toList());
    serviceApiList.forEach(it -> it.setStatus(null));
    clientServiceApiList.addAll(serviceApiList);
    clientServiceApiList.sort(Comparator.comparing(ApiClientServiceDto::getId));
    return clientServiceApiList;
  }

  @Override
  public DataPagingResponse<ServiceResponseDto> getListServiceNotSettingOnApp(
      Integer appId, String search, String systems, String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException {

    if (appId == null) {
      throw new IdentifyBlankException("App Id null",
          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
    }
    Optional<Client> clientOptional = clientRepository.findById(appId);
    if (clientOptional.isEmpty()) {
      throw new ResourceNotFoundException("app not found",
          ServiceInfo.getId() + AuthServiceMessageCode.APP_NOT_FOUND);
    }

    Set<Integer> listSystemId = new HashSet<>();
    if (systems != null && !systems.isEmpty()) {
      String[] systemArrays = systems.trim().split(",");
      for (String system : systemArrays) {
        listSystemId.add(Integer.parseInt(system));
      }
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);

    List<ClientService> clientServices = clientServiceRepository
        .findAllByClientIdAndIsDeletedFalse(appId);

    Set<Integer> serviceIds = clientServices.stream()
        .map(ClientService::getServiceId)
        .collect(Collectors.toSet());
    Specification<com.example.authservice.entities.service.Service> specification = new ServiceFilter()
        .getServiceFilter(search, listSystemId, serviceIds, map, ServiceStatus.ACTIVE, false);
    Page<com.example.authservice.entities.service.Service> servicePage = serviceRepository
        .findAll(specification, PageRequest.of(page - 1, limit));
    List<com.example.authservice.entities.service.Service> serviceList = servicePage.getContent();

    List<ServiceResponseDto> serviceResponseList = serviceList.stream()
        .map(serviceMapper::toServiceResponseDto)
        .collect(Collectors.toList());

    DataPagingResponse<ServiceResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(serviceResponseList);
    dataPagingResponses.setTotalPage(servicePage.getTotalPages());
    dataPagingResponses.setNum(servicePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public List<UserAppPermission> getAllUserAppPermission(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    this.checkValidUser(userId);
    List<ClientUser> clientUserList = clientUserRepository.findAllByUserIdAndIsDeletedFalse(userId);
    List<UserAppPermission> list = new ArrayList<>();
    for (ClientUser clientUser : clientUserList) {
      List<String> permissions = getAllPermissionByClientUserId(clientUser.getId());
      UserAppPermission userAppPermission = new UserAppPermission();
      userAppPermission.setId(clientUser.getClientId());
      userAppPermission.setPermissions(permissions);
      list.add(userAppPermission);
    }
    return list;
  }

  List<String> getAllPermissionByClientUserId(Integer id) {
    List<ClientUserPermission> clientUserPermissions = clientUserPermissionRepository
        .findAllByClientUserIdAndIsDeletedFalse(id);
    List<Integer> roleIds = clientUserPermissions.stream()
        .map(ClientUserPermission::getRole)
        .map(Role::getId)
        .collect(Collectors.toList());
    List<RoleDetail> roleDetailList = roleDetailRepository
        .findAllByRoleIdInAndIsDeletedFalse(roleIds);
    return roleDetailList.stream()
        .map(RoleDetail::getPermission)
        .distinct()
        .map(SysPermission::getCode)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @Modifying
  public String createApiKey(Integer clientId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted()
        || userOptional.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted()) {
      throw new OperationNotImplementException("client not found",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_NOT_EXIST);
    }
    if (clientOptional.get().getStatus().equals(ClientStatus.DEACTIVE)) {
      throw new OperationNotImplementException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = clientOptional.get();
    if (!client.getAuthType().equals(ClientAuthType.API_KEY)) {
      throw new ResourceNotFoundException("client auth type invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_AUTH_TYPE_INVALID);
    }
    List<ClientApiKey> listClientApiKeyActive = clientApiKeyRepository
        .findAllByClientIdAndStatusAndIsDeletedFalse(clientId, ClientApiKeyStatus.ACTIVE);
    // generate apiKey
    String apiKey = new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(
        StandardCharsets.UTF_8)));
    ClientApiKey clientApiKey = new ClientApiKey();
    clientApiKey.setApiKey(apiKey);
    clientApiKey.setClient(client);
    clientApiKey.setCreatorUser(userOptional.get());
    clientApiKey.setIsDeleted(Boolean.FALSE);
    clientApiKey.setStatus(ClientApiKeyStatus.ACTIVE);

    ClientApiKey res = clientApiKeyRepository.save(clientApiKey);
    if (!listClientApiKeyActive.isEmpty()) {
      listClientApiKeyActive.forEach(it -> {
        it.setStatus(ClientApiKeyStatus.EXPIRED);
        it.setUpdaterUser(userOptional.get());
        clientApiKeyRepository.save(it);
        String key = KeyConstants.RedisKey.AUTH_APP.concat(it.getApiKey());
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
    }
    return res.getApiKey();
  }

  @Override
  public List<ClientAuthTypeDto> getListClientAuthType() {
    List<ClientAuthTypeDto> filterList = new ArrayList<>();
    ClientAuthType[] authTypes = ClientAuthType.class.getEnumConstants();
    for (ClientAuthType type : authTypes) {
      ClientAuthTypeDto filter = new ClientAuthTypeDto();
      filter.setName(type.name());
      filter.setValue(type.getValue());
      filter.setDescription(type.getDescription());
      filterList.add(filter);
    }
    return filterList;
  }

  public DataPagingResponse<ClientApiKeyResponseDto> getListClientApiKeyForApplication(
      Integer appId, String sort, Integer page, Integer limit)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    if (appId == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + AuthServiceMessageCode.APP_ID_NULL);
    }

    Client clientById = clientRepository.findByIdAndIsDeletedFalse(appId);
    if (clientById == null) {
      throw new OperationNotImplementException("client not found",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_NOT_EXIST);
    }
    if (!ClientAuthType.API_KEY.equals(clientById.getAuthType())) {
      throw new ResourceNotFoundException("client auth type invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_AUTH_TYPE_INVALID);
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<ClientApiKey> clientApiKeyPage = clientApiKeyRepository
        .findAll(clientApiKeyFilter.filter(appId, map), PageRequest.of(page - 1, limit));
    List<ClientApiKey> clientApiKeys = clientApiKeyPage.getContent();
    List<ClientApiKeyResponseDto> clientApiKeyResponseDtos = clientApiKeys.stream()
        .map(clientApiKeyMapper::toDto).collect(Collectors.toList());
    DataPagingResponse<ClientApiKeyResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(clientApiKeyResponseDtos);
    dataPagingResponses.setTotalPage(clientApiKeyPage.getTotalPages());
    dataPagingResponses.setNum(clientApiKeyPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public void deleteApiKey(Integer id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository.findById(id);
    if (apiKeyOptional.isEmpty()) {
      throw new ResourceNotFoundException("api key not found", ServiceInfo.getId() +
          AuthServiceMessageCode.CLIENT_API_KEY_NOT_FOUND);
    }
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted()
        || userOptional.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    ClientApiKey apiKey = apiKeyOptional.get();
    if (apiKey.getStatus().equals(ClientApiKeyStatus.ACTIVE)) {
      throw new OperationNotImplementException("can't delete active api key",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_API_KEY_IS_ACTIVE);
    }
    apiKey.setIsDeleted(Boolean.TRUE);
    apiKey.setDeleterUser(userOptional.get());
    clientApiKeyRepository.save(apiKey);
    String redisKey = KeyConstants.RedisKey.AUTH_APP.concat(apiKey.getApiKey());
    if (redisService.exists(redisKey)) {
      redisService.remove(redisKey);
    }
  }

  @Override
  public void cancelApiKey(String id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted()
        || userOptional.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository.findByApiKey(id);
    if (apiKeyOptional.isEmpty()) {
      throw new ResourceNotFoundException("api key not found", ServiceInfo.getId() +
          AuthServiceMessageCode.CLIENT_API_KEY_NOT_FOUND);
    }
    ClientApiKey apiKey = apiKeyOptional.get();
    if (apiKey.getStatus().equals(ClientApiKeyStatus.EXPIRED)) {
      throw new OperationNotImplementException("can't cancel api key expired",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_API_KEY_EXPIRED);
    }
    apiKey.setStatus(ClientApiKeyStatus.EXPIRED);
    apiKey.setUpdaterUser(userOptional.get());
    clientApiKeyRepository.save(apiKey);
    String redisKey = KeyConstants.RedisKey.AUTH_APP.concat(apiKey.getApiKey());
    if (redisService.exists(redisKey)) {
      redisService.remove(redisKey);
    }
  }

  void reloadJwtOfApiKey(Integer clientId) {
    // delete cache
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository
        .findApiKeyActiveOfClient(clientId);
    if (apiKeyOptional.isEmpty()) {
      return;
    }
    ClientApiKey apiKey = apiKeyOptional.get();
    String key = KeyConstants.RedisKey.AUTH_APP.concat(apiKey.getApiKey());
    if (redisService.exists(key)) {
      redisService.remove(key);
    }
  }
}
