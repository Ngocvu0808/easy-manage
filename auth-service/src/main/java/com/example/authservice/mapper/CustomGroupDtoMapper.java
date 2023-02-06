package com.example.authservice.mapper;


import com.example.authservice.dto.auth.CustomGroupDto;
import com.example.authservice.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CustomGroupDtoMapper {

  public abstract CustomGroupDto toDto(Group group);
}
