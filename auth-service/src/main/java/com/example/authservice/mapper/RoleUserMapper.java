package com.example.authservice.mapper;

import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.entities.RoleUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleUserMapper {

  @Mapping(target = "name", source = "role.name")
  @Mapping(target = "id", source = "role.id")
  public abstract RoleCustomDto toDto(RoleUser roleUser);

}
