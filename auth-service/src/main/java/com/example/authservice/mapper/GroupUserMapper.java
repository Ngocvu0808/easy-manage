package com.example.authservice.mapper;

import com.example.authservice.dto.group.GroupUserCustomDto;
import com.example.authservice.entities.GroupUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {GroupMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class GroupUserMapper {

    @Mapping(target = "id", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    @Mapping(target = "code", source = "group.code")
    public abstract GroupUserCustomDto toDto(GroupUser groupUser);

}
