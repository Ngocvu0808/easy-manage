package com.example.authservice.mapper;

import com.example.authservice.dto.api.ApiRequestDto;
import com.example.authservice.entities.application.ApiRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Date;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class, SystemMapper.class,
    ClientMapper.class, ExternalApiMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ApiRequestMapper {

  @Mapping(target = "apiId", source = "api.id")
  @Mapping(target = "name", source = "api.name")
  @Mapping(target = "code", source = "api.code")
  @Mapping(target = "api", source = "api.api")
  @Mapping(target = "method", source = "api.method")
  @Mapping(target = "type", source = "api.type")
  @Mapping(target = "system", source = "api.service.system")
  @Mapping(target = "service", source = "api.service")
  @Mapping(target = "username", source = "creatorUser.username")
  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  public abstract ApiRequestDto toDto(ApiRequest entity);

  Long map(Date value) {
    if (value == null) {
      return null;
    }
    return value.getTime();
  }

  Date map(Long value) {
    if (value == null) {
      return null;
    }
    return new Date(value);
  }
}
