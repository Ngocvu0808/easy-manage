package com.example.authservice.service.iface;


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
import com.example.authservice.entities.enums.ApiRequestStatus;
import com.example.authservice.entities.enums.ApiStatus;
import com.example.authservice.entities.service.ExternalApi;
import com.example.authservice.utils.exception.DuplicateEntityException;
import com.example.authservice.utils.exception.IdentifyBlankException;
import com.example.authservice.utils.exception.OperationNotImplementException;
import com.example.authservice.utils.exception.ResourceNotFoundException;
import com.example.authservice.utils.response.DataPagingResponse;

import java.util.List;

public interface ServiceService {

  Integer createService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException,
          DuplicateEntityException;

  List<ApiTypeDto> getListApiType();

  List<HttpMethodDto> getListMethod();

  List<ApiStatusDto> getListApiStatus();

  DataPagingResponse<ServiceResponseDto> getServices(String systems, String status, String sort,
                                                     String search, Integer page, Integer limit);

  List<ApiRequestStatusDto> getListApiRequestStatus();

  ExternalApi addApi(ApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, DuplicateEntityException;

  Boolean updateStatusService(ServiceRequestUpdateStatusDto serviceRequestUpdateStatusDto,
                              Integer updateUser)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException;

  Boolean deleteServiceById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  List<ServiceResponseDto> getAll();

  Boolean updateService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  void changeApiStatus(Long apiId, ApiStatus status, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  ApiDto getApiById(Long apiId) throws OperationNotImplementException;

  void deleteApi(Long apiId, Integer deleterId)
      throws OperationNotImplementException, ResourceNotFoundException;

  DataPagingResponse<ApiDto> getApis(String systems, String services, String types, String status,
      String sort, String search, Integer page, Integer limit);

  DataPagingResponse<ApiRequestDto> getApiRequests(String systems, String services, String clients,
                                                   String types, String status, String sort, String search, Integer page, Integer limit);

  ApiRequestDto getApiRequestById(Long apiId) throws ResourceNotFoundException;

  void changeStatusApiRequest(Long apiId, ApiRequestStatus status, Integer updaterId)
      throws ResourceNotFoundException;

  ApiDto updateApi(Long apiId, ApiUpdateDto dto, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  ServiceResponseDto getServiceById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException;
}
