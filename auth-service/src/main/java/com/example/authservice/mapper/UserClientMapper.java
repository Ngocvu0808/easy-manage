package com.example.authservice.mapper;

import com.example.authservice.dto.app.UserClientCustomDto;
import com.example.authservice.entities.application.ClientUser;
import com.example.authservice.entities.user.User;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

/**
 * @author bontk
 * @date 26/02/2020
 */

@Mapper(componentModel = "spring", uses = {ClientUserPermissionMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class UserClientMapper {

  @Mapping(target = "id",source = "user.id")
  @Mapping(target = "username",source = "user.username")
  @Mapping(target = "name",source = "user.name")
  @Mapping(target = "email",source = "user.email")
  @Mapping(target = "status",source = "user.status")
  public abstract UserClientCustomDto toDto(ClientUser clientUser);

  public abstract User fromDto(UserClientCustomDto dto);

  @BeforeMapping
  public void validSourceDto(UserClientCustomDto dto) {
    dto.setName(dto.getName().trim());
    dto.setUsername(dto.getUsername().trim());
  }


  public abstract void updateModel(@MappingTarget User user, UserClientCustomDto dto);

}
