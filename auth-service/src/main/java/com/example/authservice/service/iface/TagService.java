package com.example.authservice.service.iface;

import com.example.authservice.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {

  List<TagResponseDto> getTags();
}
