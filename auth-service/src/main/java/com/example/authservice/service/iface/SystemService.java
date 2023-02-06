package com.example.authservice.service.iface;

import com.example.authservice.dto.appservice.SystemCustomDto;

import java.util.List;

public interface SystemService {

  List<SystemCustomDto> getSystems();

}
