package com.example.authservice.service.iface;

import com.example.authservice.dto.app.*;
import com.example.authservice.dto.auth.GuestAccessRequestDto;
import com.example.authservice.dto.auth.GuestAccessResponseDto;
import com.example.authservice.dto.auth.LoginRequestDto;
import com.example.authservice.dto.auth.LoginResponseDto;
import com.example.authservice.entities.application.Client;
import com.example.authservice.entities.enums.ClientStatus;
import com.example.authservice.entities.enums.RefreshTokenStatus;
import com.example.authservice.utils.exception.*;
import com.example.authservice.utils.response.DataPagingResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface AuthService {

  LoginResponseDto checkAuthenticate(HttpServletRequest request) throws UnAuthorizedException;

  LoginResponseDto reloadPermission(HttpServletRequest request)
      throws UnAuthorizedException, IdentifyBlankException;

  Boolean validateToken(HttpServletRequest request) throws UnAuthorizedException;

  void logout(HttpServletRequest request)
      throws UnAuthorizedException, ServletException, UnknownHostException;

  LoginResponseDto login(HttpServletRequest request, LoginRequestDto loginRequestDto)
      throws ResourceNotFoundException, IdentifyBlankException, UnAuthorizedException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, CryptoException, UnknownHostException;

  void reloadPermission(List<Integer> userIdList);

  String genAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  String getExistAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  String reloadAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  Client addClient(ClientRequestDto clientRequestDto, Integer creatorId)
      throws IdentifyBlankException, OperationNotImplementException, DuplicateEntityException, ResourceNotFoundException;

  DataPagingResponse<ClientResponseDto> getAllClient(Integer userId, Integer page, Integer limit,
      String search, ClientStatus status, String sort, Boolean isGetAll);

  ClientDetailDto getClientById(Integer id) throws ResourceNotFoundException;

  void updateClient(Integer id, UpdateClientDto dto) throws ResourceNotFoundException;

  void deleteClient(Integer id) throws ResourceNotFoundException, OperationNotImplementException;

  Client changeStatusClient(Integer clientId, ClientStatus status)
      throws ResourceNotFoundException, OperationNotImplementException;

  // add, remove ip
  void addIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException;

  void removeIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException;

  ClientWhiteListResponseDto getAllIpOfClient(Integer userId, Integer clientId)
      throws ResourceNotFoundException;

  Map<String, Object> getToken(HttpServletRequest request, TokenRequestDto tokenRequestDto)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException, UnAuthorizedException;

  // Approve, un approve refresh token
  void approveToken(HttpServletRequest request, Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void unApproveToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void changeTokenStatus(Long refreshTokenId, Integer userId, RefreshTokenStatus status)
      throws ResourceNotFoundException, OperationNotImplementException;

  void deleteToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  Map<String, Object> getAccessTokenFromRefreshToken(HttpServletRequest request,
      String refreshToken)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException, UnAuthorizedException;

  DataPagingResponse<RefreshTokenDto> getAllRefreshTokenOfUser(Integer userId, Integer page,
      Integer limit, String status, String sort) throws ResourceNotFoundException;

  GuestAccessResponseDto guestAccess(GuestAccessRequestDto request) throws NoSuchAlgorithmException;

  void temporaryClose(HttpServletRequest request) throws ResourceNotFoundException;

  void saveUserActivity(UserActivityRequestDto dto, HttpServletRequest request)
      throws UnknownHostException;
}
