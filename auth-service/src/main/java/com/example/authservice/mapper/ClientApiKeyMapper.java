package com.example.authservice.mapper;


import com.example.authservice.dto.app.ClientApiKeyResponseDto;
import com.example.authservice.entities.application.ClientApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ClientApiKeyMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientApiKeyResponseDto toDto(ClientApiKey entity);
}
