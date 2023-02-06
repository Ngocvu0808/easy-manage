package com.example.authservice.mapper;


import com.example.authservice.dto.refreshtoken.AccessTokenResponseDto;
import com.example.authservice.entities.application.AccessToken;
import com.example.authservice.entities.application.RefreshToken;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class AccessTokenMapper {

  public abstract AccessTokenResponseDto toAccessTokenResponseDto(AccessToken accessToken);

  @AfterMapping
  public void afterMapping(@MappingTarget AccessTokenResponseDto accessTokenResponseDto,
      AccessToken accessToken) {
    RefreshToken refreshToken = accessToken.getRefreshToken();
    if (refreshToken != null) {
      accessTokenResponseDto.setIp(refreshToken.getIp());
      accessTokenResponseDto.setRefresh(refreshToken.getToken().substring(0, 15) + "...");
      accessTokenResponseDto.setToken(accessToken.getToken().substring(0, 15) + "...");
    }

  }
}
