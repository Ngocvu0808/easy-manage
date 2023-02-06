package com.example.authservice.mapper;


import com.example.authservice.dto.app.ClientUserPermissionDto;
import com.example.authservice.entities.application.ClientUserPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ClientUserPermissionMapper {

  @Mapping(target = "roleName", source = "role.name")
  @Mapping(target = "roleId", source = "role.id")
  public abstract ClientUserPermissionDto toDto(ClientUserPermission permission);

}
