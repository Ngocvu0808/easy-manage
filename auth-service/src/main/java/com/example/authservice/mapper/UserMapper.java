package com.example.authservice.mapper;

import com.example.authservice.dto.user.UserDto;
import com.example.authservice.entities.user.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

/**
 * @author bontk
 * @date 26/02/2020
 */

@Mapper(componentModel = "spring", uses = {RoleUserMapper.class,},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class UserMapper {

  public abstract UserDto toDto(User user);

  public abstract User fromDto(UserDto dto);

  @BeforeMapping
  public void validSourceDto(UserDto dto) {
    dto.setName(dto.getName().trim());
    dto.setUsername(dto.getUsername().trim());
    dto.setFirstName(dto.getFirstName().trim());
    dto.setLastName(dto.getLastName().trim());
  }


  public abstract void updateModel(@MappingTarget User user, UserDto dto);

}
