package com.example.authservice.mapper;

import com.example.authservice.dto.SysPermissionDto;
import com.example.authservice.entities.SysPermission;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class SysPermissionMapper {

  public abstract SysPermissionDto toDto(SysPermission entity);

  public abstract SysPermission fromDto(SysPermissionDto dto);

  @BeforeMapping
  public void validSourceDto(SysPermissionDto dto) {
    dto.setName(dto.getName().trim());
    dto.setCode(dto.getCode().trim());
  }


  public abstract void updateModel(@MappingTarget SysPermission entity, SysPermissionDto dto);


}
