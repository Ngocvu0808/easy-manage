package com.example.authservice.mapper;

import com.example.authservice.dto.tag.CustomTagResponseDto;
import com.example.authservice.dto.tag.TagResponseDto;
import com.example.authservice.entities.service.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class TagMapper {

  public abstract TagResponseDto toDto(Tag tag);

  public abstract CustomTagResponseDto toCustomTag(Tag tag);
}
