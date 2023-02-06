package com.example.authservice.mapper;


import com.example.authservice.dto.app.ClientCustomDto;
import com.example.authservice.dto.app.ClientDetailDto;
import com.example.authservice.dto.app.ClientResponseDto;
import com.example.authservice.entities.application.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Date;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ClientMapper {

  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientDetailDto toDto(Client entity);

  public abstract ClientCustomDto toCustomDto(Client entity);

  public abstract Client fromDto(ClientDetailDto dto);

  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientResponseDto getClient(Client entity);

  public abstract void updateModel(@MappingTarget Client entity, ClientDetailDto dto);

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
