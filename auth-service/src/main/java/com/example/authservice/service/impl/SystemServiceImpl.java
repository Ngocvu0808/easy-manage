package com.example.authservice.service.impl;


import com.example.authservice.dto.appservice.SystemCustomDto;
import com.example.authservice.entities.service.System;
import com.example.authservice.mapper.SystemMapper;
import com.example.authservice.repo.SystemRepository;
import com.example.authservice.service.iface.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {

  @Autowired
  private SystemRepository systemRepository;

  @Autowired
  private SystemMapper systemMapper;

  @Override
  public List<SystemCustomDto> getSystems() {
    List<System> systems = systemRepository.findAll();
    List<SystemCustomDto> systemCustomDtos = new ArrayList<>();
    systems.forEach(system -> systemCustomDtos.add(systemMapper.toDto(system)));
    return systemCustomDtos;
  }
}
