package com.example.authservice.service.impl;

import com.example.authservice.entities.enums.AccessTokenStatusFilter;
import com.example.authservice.service.iface.AccessTokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

  @Override
  public List<AccessTokenStatusFilter> getStatusOfAccessToken() {
    return Stream.of(AccessTokenStatusFilter.values()).collect(Collectors.toList());
  }
}
