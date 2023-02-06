package com.example.authservice.service.impl;

import com.example.authservice.dto.api.ApiAddDto;
import com.example.authservice.dto.api.ApiDto;
import com.example.authservice.dto.api.ApiRequestDto;
import com.example.authservice.dto.api.ApiUpdateDto;
import com.example.authservice.dto.filter.ApiRequestStatusDto;
import com.example.authservice.dto.filter.ApiStatusDto;
import com.example.authservice.dto.filter.ApiTypeDto;
import com.example.authservice.dto.filter.HttpMethodDto;
import com.example.authservice.dto.service.ServiceRequestDto;
import com.example.authservice.dto.service.ServiceRequestUpdateStatusDto;
import com.example.authservice.dto.service.ServiceResponseDto;
import com.example.authservice.entities.UserStatus;
import com.example.authservice.entities.application.ApiRequest;
import com.example.authservice.entities.application.ClientApi;
import com.example.authservice.entities.application.ClientApiKey;
import com.example.authservice.entities.enums.*;
import com.example.authservice.entities.service.ExternalApi;
import com.example.authservice.entities.service.ServiceTag;
import com.example.authservice.entities.service.System;
import com.example.authservice.entities.service.Tag;
import com.example.authservice.entities.user.User;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.filter.ApiFilter;
import com.example.authservice.filter.ApiRequestFilter;
import com.example.authservice.filter.ServiceFilter;
import com.example.authservice.mapper.ApiRequestMapper;
import com.example.authservice.mapper.ExternalApiMapper;
import com.example.authservice.mapper.ServiceMapper;
import com.example.authservice.repo.*;
import com.example.authservice.service.iface.ServiceService;
import com.example.authservice.utils.KeyConstants;
import com.example.authservice.utils.ServiceInfo;
import com.example.authservice.utils.SortingUtils;
import com.example.authservice.utils.Utils;
import com.example.authservice.utils.cache.CacheRedisService;
import com.example.authservice.utils.exception.DuplicateEntityException;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;
  private final SystemRepository systemRepository;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;
  private final ServiceTagRepository serviceTagRepository;
  private final ExternalApiRepository externalApiRepository;
  private final ApiRequestRepository apiRequestRepository;
  private final ExternalApiMapper externalApiMapper;
  private final ApiRequestMapper apiRequestMapper;
  private final ClientApiRepository clientApiRepository;
  private final ClientApiKeyRepository clientApiKeyRepository;
  private final CacheRedisService redisService;

  public ServiceServiceImpl(ServiceRepository serviceRepository,
      ServiceMapper serviceMapper,
      SystemRepository systemRepository,
      UserRepository userRepository,
      TagRepository tagRepository,
      ServiceTagRepository serviceTagRepository,
      ExternalApiRepository externalApiRepository,
      ApiRequestRepository apiRequestRepository,
      ExternalApiMapper externalApiMapper,
      ApiRequestMapper apiRequestMapper,
      ClientApiRepository clientApiRepository,
      ClientApiKeyRepository clientApiKeyRepository,
      CacheRedisService redisService) {
    this.serviceRepository = serviceRepository;
    this.serviceMapper = serviceMapper;
    this.systemRepository = systemRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.serviceTagRepository = serviceTagRepository;
    this.externalApiRepository = externalApiRepository;
    this.apiRequestRepository = apiRequestRepository;
    this.externalApiMapper = externalApiMapper;
    this.apiRequestMapper = apiRequestMapper;
    this.clientApiRepository = clientApiRepository;
    this.clientApiKeyRepository = clientApiKeyRepository;
    this.redisService = redisService;
  }

  @Override
  public Integer createService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException, DuplicateEntityException {

    if (serviceRequestDto == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    if (serviceRequestDto.getSystemId() == null) {
      throw new IdentifyBlankException("System id null",
          ServiceInfo.getId() + AuthServiceMessageCode.SYSTEM_ID_NULL);
    }

    if (serviceRequestDto.getCode() == null) {
      throw new IdentifyBlankException("Code null",
          ServiceInfo.getId() + AuthServiceMessageCode.CODE_SERVICE_NULL);
    }

    User user = checkValidUser(userId);

    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findByCodeAndSystemIdAndIsDeletedFalse(serviceRequestDto.getCode(),
            serviceRequestDto.getSystemId());
    if (serviceOptional.isPresent()) {
      throw new DuplicateEntityException("Code exists",
          ServiceInfo.getId() + AuthServiceMessageCode.CODE_EXISTS);
    }

    Optional<System> optionalSystemById = systemRepository
        .findById(serviceRequestDto.getSystemId());
    if (optionalSystemById.isEmpty()) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SYSTEM_NOT_FOUND);
    }

    com.example.authservice.entities.service.Service service = serviceMapper
        .fromServiceRequestDto(serviceRequestDto);
    service.setSystem(optionalSystemById.get());
    service.setStatus(ServiceStatus.ACTIVE);
    service.setCreatorUser(user);

    com.example.authservice.entities.service.Service serviceSaved = serviceRepository.save(service);

    addTagToService(serviceRequestDto, service);

    return serviceSaved.getId();
  }


  public User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepository.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Resource not found" + userId,
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_DEACTIVE);
    }
    return userOptionalById.get();
  }

  @Override
  public List<ApiTypeDto> getListApiType() {
    List<ApiTypeDto> typeList = new ArrayList<>();
    ApiType[] apiTypes = ApiType.class.getEnumConstants();
    for (ApiType type : apiTypes) {
      ApiTypeDto dto = new ApiTypeDto();
      dto.setName(type.name());
      dto.setValue(type.getValue());
      dto.setDescription(type.getDescription());
      typeList.add(dto);
    }
    return typeList;
  }

  @Override
  public List<HttpMethodDto> getListMethod() {
    List<HttpMethodDto> methodList = new ArrayList<>();
    HttpMethod[] methods = HttpMethod.class.getEnumConstants();
    for (HttpMethod method : methods) {
      HttpMethodDto dto = new HttpMethodDto();
      dto.setName(method.name());
      dto.setValue(method.getValue());
      methodList.add(dto);
    }
    return methodList;
  }

  @Override
  public List<ApiStatusDto> getListApiStatus() {
    List<ApiStatusDto> statusList = new ArrayList<>();
    ApiStatus[] statuses = ApiStatus.class.getEnumConstants();
    for (ApiStatus status : statuses) {
      ApiStatusDto dto = new ApiStatusDto();
      dto.setName(status.name());
      dto.setValue(status.getValue());
      statusList.add(dto);
    }
    return statusList;
  }

  @Override
  public List<ApiRequestStatusDto> getListApiRequestStatus() {
    List<ApiRequestStatusDto> statusList = new ArrayList<>();
    ApiRequestStatus[] statuses = ApiRequestStatus.class.getEnumConstants();
    for (ApiRequestStatus status : statuses) {
      ApiRequestStatusDto dto = new ApiRequestStatusDto();
      dto.setName(status.name());
      dto.setValue(status.getValue());
      statusList.add(dto);
    }
    return statusList;
  }

  @Override
  public Boolean updateStatusService(ServiceRequestUpdateStatusDto serviceRequestUpdateStatusDto,
                                     Integer updateUser)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException {
    if (serviceRequestUpdateStatusDto == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    if (serviceRequestUpdateStatusDto.getId() == null) {
      throw new IdentifyBlankException("Id service null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_SERVICE_NULL);
    }

    if (serviceRequestUpdateStatusDto.getStatus() == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    User user = checkValidUser(updateUser);

    com.example.authservice.entities.service.Service serviceById = serviceRepository
        .findByIdAndIsDeletedFalse(serviceRequestUpdateStatusDto.getId());

    if (serviceById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }

    if (serviceById.getStatus().equals(serviceRequestUpdateStatusDto.getStatus())) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    serviceById.setStatus(serviceRequestUpdateStatusDto.getStatus());
    serviceById.setUpdaterUser(user);
    serviceRepository.save(serviceById);
    return true;
  }

  @Override
  public Boolean deleteServiceById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("Id service null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_SERVICE_NULL);
    }

    checkValidUser(userId);

    com.example.authservice.entities.service.Service serviceById = serviceRepository
        .findByIdAndIsDeletedFalse(id);

    if (serviceById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    serviceById.setIsDeleted(Boolean.TRUE);
    serviceById.setDeleterUserId(userId);
    serviceRepository.save(serviceById);
    return true;
  }

  @Override
  public List<ServiceResponseDto> getAll() {
    List<com.example.authservice.entities.service.Service> services = serviceRepository
        .findAllByIsDeletedFalse();
    return services.stream()
        .filter(it -> it.getStatus().equals(ServiceStatus.ACTIVE))
        .map(serviceMapper::toServiceResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public Boolean updateService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {

    if (serviceRequestDto == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    if (serviceRequestDto.getId() == null) {
      throw new IdentifyBlankException("Id service null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_SERVICE_NULL);
    }

    com.example.authservice.entities.service.Service serviceById = serviceRepository
        .findByIdAndIsDeletedFalse(serviceRequestDto.getId());

    if (serviceById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + AuthServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    //delete tag
    List<ServiceTag> serviceTags = new ArrayList<>(serviceById.getServiceTags());

    for (ServiceTag serviceTag : serviceTags) {
      serviceTag.setIsDeleted(Boolean.TRUE);
      serviceTagRepository.save(serviceTag);
    }

    serviceMapper.updateModel(serviceById, serviceRequestDto);
    serviceRepository.save(serviceById);
    addTagToService(serviceRequestDto, serviceById);

    return true;
  }

  public void addTagToService(ServiceRequestDto serviceRequestDto,
      com.example.authservice.entities.service.Service service) throws ResourceNotFoundException {

    // add tag
    Set<Long> tagIds = serviceRequestDto.getTagIds();

    if (tagIds == null) {
      tagIds = new HashSet<>();
    }

    List<String> listTagName = serviceRequestDto.getListTagName();

    if (listTagName != null) {
      for (String tagName : listTagName) {
        Tag tagByName = tagRepository.findByTag(tagName);
        if (tagByName == null) {
          Tag tag = new Tag();
          tag.setTag(tagName);
          tagIds.add(tagRepository.save(tag).getId());
        }
      }
    }

    List<Tag> listTag = new ArrayList<>();
    if (tagIds != null) {
      for (Long tagId : tagIds) {
        Optional<Tag> optionalTagById = tagRepository.findById(tagId);
        if (optionalTagById.isEmpty()) {
          throw new ResourceNotFoundException("Tag not found Exception",
              ServiceInfo.getId() + AuthServiceMessageCode.TAG_NOT_FOUND);
        }
        listTag.add(optionalTagById.get());
      }

    }

    for (Tag tag : listTag) {
      ServiceTag serviceTag = new ServiceTag();
      serviceTag.setService(service);
      serviceTag.setTag(tag);
      serviceTagRepository.save(serviceTag);
    }

  }

  @Override
  public DataPagingResponse<ServiceResponseDto> getServices(String systems, String status,
                                                            String sort,
                                                            String search, Integer limit, Integer page) {
    Set<Integer> listSystemId = new HashSet<>();
    if (systems != null && !systems.isEmpty()) {
      String[] systemArrays = systems.trim().split(",");
      for (String system : systemArrays) {
        listSystemId.add(Integer.parseInt(system));
      }
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);

    Set<ServiceStatus> listStatus = new HashSet<>();
    if (status != null && !status.isEmpty()) {
      String[] statusStringArrays = status.trim().split(",");
      for (String statusString : statusStringArrays) {

        if (statusString.equals(ServiceStatus.ACTIVE.name())) {
          listStatus.add(ServiceStatus.ACTIVE);
        }

        if (statusString.equals(ServiceStatus.DEACTIVE.name())) {
          listStatus.add(ServiceStatus.DEACTIVE);
        }
      }
    }

    Page<com.example.authservice.entities.service.Service> pageService = serviceRepository
        .findAll(new ServiceFilter().getByFilter(search, listSystemId, listStatus, map, false),
            PageRequest.of(page - 1, limit));
    List<ServiceResponseDto> serviceResponseDtos = new ArrayList<>();

    pageService.getContent()
        .forEach(service -> serviceResponseDtos.add(serviceMapper.toServiceResponseDto(service)));

    DataPagingResponse<ServiceResponseDto> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(serviceResponseDtos);
    dataPagingResponse.setNum(pageService.getTotalElements());
    dataPagingResponse.setTotalPage(pageService.getTotalPages());
    dataPagingResponse.setCurrentPage(page);

    return dataPagingResponse;
  }

  @Override
  public ExternalApi addApi(ApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, DuplicateEntityException {
    User user = this.checkValidUser(creatorId);
    Optional<ExternalApi> externalApiOptional = externalApiRepository
        .findByCodeAndServiceIdAndIsDeletedFalse(dto.getCode(), dto.getServiceId());
    if (externalApiOptional.isPresent()) {
      throw new DuplicateEntityException("api already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.API_ALREADY_EXIST);
    }
    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(dto.getServiceId());
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }

    ExternalApi externalApi = externalApiMapper.fromApiAddDto(dto);
    externalApi.setService(serviceOptional.get());
    externalApi.setStatus(ApiStatus.ACTIVE);
    externalApi.setIsDeleted(Boolean.FALSE);
    externalApi.setCreatorUser(user);

    return externalApiRepository.save(externalApi);
  }

  @Override
  public void changeApiStatus(Long apiId, ApiStatus status, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User user = this.checkValidUser(updaterId);
    Optional<ExternalApi> externalApiOptional = externalApiRepository.findById(apiId);
    if (externalApiOptional.isEmpty() || externalApiOptional.get().getIsDeleted()) {
      throw new OperationNotImplementException("api not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_NOT_FOUND);
    }
    ExternalApi externalApi = externalApiOptional.get();
    externalApi.setUpdaterUser(user);
    externalApi.setStatus(status);

    externalApiRepository.save(externalApi);
  }

  @Override
  public ApiDto getApiById(Long apiId) throws OperationNotImplementException {
    Optional<ExternalApi> externalApiOptional = externalApiRepository.findById(apiId);
    if (externalApiOptional.isEmpty() || externalApiOptional.get().getIsDeleted()) {
      throw new OperationNotImplementException("api not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_NOT_FOUND);
    }
    ExternalApi externalApi = externalApiOptional.get();
    return externalApiMapper.toDto(externalApi);
  }

  @Override
  public void deleteApi(Long apiId, Integer deleterId)
      throws OperationNotImplementException, ResourceNotFoundException {
    this.checkValidUser(deleterId);
    Optional<ExternalApi> externalApiOptional = externalApiRepository.findById(apiId);
    if (externalApiOptional.isEmpty() || externalApiOptional.get().getIsDeleted()) {
      throw new OperationNotImplementException("api not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_NOT_FOUND);
    }

    ExternalApi externalApi = externalApiOptional.get();
    externalApi.setIsDeleted(Boolean.TRUE);
    externalApi.setDeleterUserId(deleterId);
    externalApiRepository.save(externalApi);
  }

  @Override
  public DataPagingResponse<ApiDto> getApis(String systems, String services, String types,
      String status, String sort, String search, Integer page, Integer limit) {
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Set<Integer> systemIdList = Utils.strToIntegerSet(systems);
    Set<Integer> serviceIdList = Utils.strToIntegerSet(services);
    Set<String> statusList = Utils.strToStringSet(status);
    Set<String> typeList = Utils.strToStringSet(types);

    Page<ExternalApi> apiPage = externalApiRepository.findAll(
        new ApiFilter().filter(systemIdList, serviceIdList, statusList, typeList, search, sortMap),
        PageRequest.of(page - 1, limit));
    List<ApiDto> apiDtoList = apiPage.getContent().stream()
        .map(externalApiMapper::toDto)
        .collect(Collectors.toList());
    DataPagingResponse<ApiDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(apiDtoList);
    dataPagingResponses.setTotalPage(apiPage.getTotalPages());
    dataPagingResponses.setNum(apiPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public DataPagingResponse<ApiRequestDto> getApiRequests(String systems, String services,
      String clients, String types, String status, String sort, String search, Integer page,
      Integer limit) {
    Set<Integer> systemIdList = Utils.strToIntegerSet(systems);
    Set<Integer> serviceIdList = Utils.strToIntegerSet(services);
    Set<Integer> clientIdList = Utils.strToIntegerSet(clients);
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Set<String> statusList = Utils.strToStringSet(status);
    Set<String> typeList = Utils.strToStringSet(types);
    Page<ApiRequest> apiPage = apiRequestRepository.findAll(
        new ApiRequestFilter()
            .filter(systemIdList, serviceIdList, clientIdList, statusList, typeList, search,
                sortMap), PageRequest.of(page - 1, limit));
    List<ApiRequest> apis = apiPage.getContent();
    List<ApiRequestDto> apiDtoList = apis.stream()
        .map(apiRequestMapper::toDto)
        .collect(Collectors.toList());
    DataPagingResponse<ApiRequestDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(apiDtoList);
    dataPagingResponses.setTotalPage(apiPage.getTotalPages());
    dataPagingResponses.setNum(apiPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public ApiRequestDto getApiRequestById(Long apiId) throws ResourceNotFoundException {
    Optional<ApiRequest> apiRequestOptional = apiRequestRepository.findById(apiId);
    if (apiRequestOptional.isEmpty() || apiRequestOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("api request not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_REQUEST_NOT_FOUND);
    }
    ApiRequest apiRequest = apiRequestOptional.get();
    return apiRequestMapper.toDto(apiRequest);
  }

  @Override
  public void changeStatusApiRequest(Long apiRequestId, ApiRequestStatus status, Integer updaterId)
      throws ResourceNotFoundException {
    Optional<ApiRequest> apiRequestOptional = apiRequestRepository.findById(apiRequestId);
    if (apiRequestOptional.isEmpty() || apiRequestOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("api request not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_REQUEST_NOT_FOUND);
    }
    ApiRequest apiRequest = apiRequestOptional.get();
    apiRequest.setStatus(status);
    apiRequest.setUpdaterUserI(updaterId);
    apiRequestRepository.save(apiRequest);
    if (status.equals(ApiRequestStatus.APPROVED)) {
      this.reloadJwtOfApiKey(apiRequest.getClientId());
    }

    Optional<ClientApi> clientApiOptional = clientApiRepository
        .findByClientIdAndApiIdAndIsDeletedFalse(apiRequest.getClientId(), apiRequest.getApiId());
    if (clientApiOptional.isPresent()) {
      ClientApi clientApi = clientApiOptional.get();
      if (status.equals(ApiRequestStatus.APPROVED)) {
        clientApi.setStatus(ClientApiStatus.ACTIVE);
        clientApi.setUpdaterUserId(updaterId);
      }
      if (status.equals(ApiRequestStatus.REJECTED)) {
        clientApi.setDeleted(Boolean.TRUE);
        clientApi.setDeleterUserId(updaterId);
      }
      clientApiRepository.save(clientApi);
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

  @Override
  public ApiDto updateApi(Long apiId, ApiUpdateDto dto, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User user = this.checkValidUser(updaterId);
    Optional<ExternalApi> apiOptional = externalApiRepository.findById(apiId);
    if (apiOptional.isEmpty() || apiOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("api not found",
          ServiceInfo.getId() + AuthServiceMessageCode.API_NOT_FOUND);
    }
    Optional<com.example.authservice.entities.service.Service> serviceOptional = serviceRepository
        .findById(dto.getServiceId());
    if (serviceOptional.isEmpty() || serviceOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("service not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    com.example.authservice.entities.service.Service service = serviceOptional.get();
    if (service.getSystem().getIsDeleted()) {
      throw new ResourceNotFoundException("service invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_INVALID);
    }
    ExternalApi api = apiOptional.get();
    externalApiMapper.updateModel(api, dto);
    api.setService(service);
    api.setUpdaterUser(user);

    ExternalApi apiRes = externalApiRepository.save(api);
    return externalApiMapper.toDto(apiRes);
  }

  @Override
  public ServiceResponseDto getServiceById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException {

    if (id == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + AuthServiceMessageCode.ID_SERVICE_NULL);
    }

    com.example.authservice.entities.service.Service serviceById = serviceRepository
        .findByIdAndIsDeletedFalse(id);

    if (serviceById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + AuthServiceMessageCode.SERVICE_NOT_FOUND);
    }
    ServiceResponseDto serviceResponseDto = serviceMapper.toServiceResponseDto(serviceById);

    return serviceResponseDto;
  }
}
