package com.example.authservice.service.impl;

import com.example.authservice.dto.tag.TagResponseDto;
import com.example.authservice.entities.service.Tag;
import com.example.authservice.mapper.TagMapper;
import com.example.authservice.repo.TagRepository;
import com.example.authservice.service.iface.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagRepositoryImpl implements TagService {

  @Autowired
  private TagRepository tagRepository;

  @Autowired
  private TagMapper tagMapper;


  @Override
  public List<TagResponseDto> getTags() {
    List<Tag> tags = tagRepository.findAll();
    List<TagResponseDto> tagResponseDtos = new ArrayList<>();
    tags.forEach(tag -> tagResponseDtos.add(tagMapper.toDto(tag)));
    return tagResponseDtos;
  }
}
