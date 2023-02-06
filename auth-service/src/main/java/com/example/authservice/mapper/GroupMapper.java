package com.example.authservice.mapper;

import com.example.authservice.dto.group.GroupDto;
import com.example.authservice.entities.Group;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoleGroupMapper.class, UserGroupMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class GroupMapper {

    @BeforeMapping
    public void validData(GroupDto groupDto) {
        groupDto.setCode(groupDto.getCode().trim());
        groupDto.setName(groupDto.getName().trim());
    }


    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "users", source = "users")
    public abstract GroupDto toDto(Group group);

    public abstract Group fromDto(GroupDto groupDto);

    @Mapping(target = "createdTime", source = "createdTime", ignore = true)
    public abstract void updateModel(@MappingTarget Group group, GroupDto groupDto);

}