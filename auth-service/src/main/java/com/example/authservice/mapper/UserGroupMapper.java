package com.example.authservice.mapper;


import com.example.authservice.dto.UserGroupCustomDto;
import com.example.authservice.entities.GroupUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {UserMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class UserGroupMapper {

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "email", source = "user.email")
  public abstract UserGroupCustomDto toDto(GroupUser groupUser);

}
