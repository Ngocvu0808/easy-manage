package com.example.authservice.mapper;


import com.example.authservice.dto.api.ApiAddDto;
import com.example.authservice.dto.api.ApiDto;
import com.example.authservice.dto.api.ApiUpdateDto;
import com.example.authservice.dto.appservice.ApiClientServiceDto;
import com.example.authservice.dto.appservice.ExternalApiCustomDto;
import com.example.authservice.entities.service.ExternalApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Date;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class, SystemMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ExternalApiMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  @Mapping(target = "updaterName", source = "updaterUser.username")
  @Mapping(target = "system", source = "service.system")
  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  @Mapping(source = "modifiedTime", target = "modifiedTime", resultType = Long.class)
  public abstract ApiDto toDto(ExternalApi entity);

  public abstract ExternalApi fromDto(ApiDto dto);

  public abstract ExternalApiCustomDto toCustomDto(ExternalApi entity);

  public abstract ApiClientServiceDto toApiClientServiceDto(ExternalApi entity);

  public abstract ExternalApi fromApiAddDto(ApiAddDto dto);


  public abstract void updateModel(@MappingTarget ExternalApi api, ApiUpdateDto dto);

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
