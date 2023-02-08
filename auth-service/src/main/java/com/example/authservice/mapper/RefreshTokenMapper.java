package com.example.authservice.mapper;

import com.example.authservice.dto.app.RefreshTokenDto;
import com.example.authservice.dto.refreshtoken.RefreshTokenResponseDto;
import com.example.authservice.entities.application.RefreshToken;
import com.example.authservice.entities.enums.RefreshTokenStatus;
import org.mapstruct.*;

import java.util.Date;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RefreshTokenMapper {

  @Mapping(target = "createdTime", source = "createdTime", resultType = Long.class)
  @Mapping(target = "clientName", source = "client.name")
  @Mapping(target = "clientId", source = "client.clientId")
  @Mapping(target = "clientSecret", source = "client.clientSecret")
  public abstract RefreshTokenDto toDto(RefreshToken entity);

  @AfterMapping
  public void afterMapping(@MappingTarget RefreshTokenResponseDto refreshTokenResponseDto,
      RefreshToken refreshToken) {
    refreshTokenResponseDto.setToken(refreshToken.getToken().substring(0, 15) + "...");
    if (refreshToken.getExpireTime() <= System.currentTimeMillis()) {
      refreshTokenResponseDto.setStatus(RefreshTokenStatus.EXPIRED);
    }
  }

  @Mapping(target = "developer", source = "apiKey.user.username")
  public abstract RefreshTokenResponseDto toRefreshTokenResponseDto(RefreshToken refreshToken);

  public abstract RefreshToken fromDto(RefreshTokenDto dto);

  Long map(Date value) {
    return value.getTime();
  }

  Date map(Long value) {
    return new Date(value);
  }
}
