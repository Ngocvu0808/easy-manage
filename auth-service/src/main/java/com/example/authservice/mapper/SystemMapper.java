package com.example.authservice.mapper;

import com.example.authservice.dto.appservice.SystemCustomDto;
import com.example.authservice.entities.service.System;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class SystemMapper {

  public abstract SystemCustomDto toDto(System api);

  public abstract System fromDto(SystemCustomDto apiDto);
}
