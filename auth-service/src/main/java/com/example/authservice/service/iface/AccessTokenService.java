package com.example.authservice.service.iface;


import com.example.authservice.entities.enums.AccessTokenStatusFilter;

import java.util.List;

public interface AccessTokenService {

  List<AccessTokenStatusFilter> getStatusOfAccessToken();
}
