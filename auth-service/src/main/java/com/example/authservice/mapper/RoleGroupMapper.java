package com.example.authservice.mapper;

import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.entities.RoleGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleGroupMapper {

  @Mapping(target = "name", source = "role.name")
  @Mapping(target = "id", source = "role.id")
  public abstract RoleCustomDto toDto(RoleGroup roleGroup);

}
