package com.example.authservice.mapper;


import com.example.authservice.dto.auth.RoleDtoExtended;
import com.example.authservice.dto.role.RoleCustomDto;
import com.example.authservice.dto.role.RoleDto;
import com.example.authservice.entities.role.Role;
import org.mapstruct.*;

/**
 * @author bontk
 * @date 05/03/2020
 */

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract RoleDto toDto(Role entity);

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract RoleDtoExtended toExtendedDto(Role entity);

  public abstract Role fromDto(RoleDto dto);

  public abstract RoleCustomDto toRoleCustomerDto(Role role);

  @BeforeMapping
  public void validSourceDto(RoleDto dto) {
    dto.setCode(dto.getCode().trim());
    dto.setNote(dto.getNote().trim());
  }

  public abstract void updateModel(@MappingTarget Role entity, RoleDto dto);


}
