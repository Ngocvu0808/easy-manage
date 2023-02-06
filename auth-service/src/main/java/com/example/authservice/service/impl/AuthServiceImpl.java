package com.example.authservice.service.impl;

import com.example.authservice.config.Constants;
import com.example.authservice.config.GenerateUniqueKey;
import com.example.authservice.config.JWTGenerator;
import com.example.authservice.config.PermissionObjectCode;
import com.example.authservice.dto.app.*;
import com.example.authservice.dto.auth.GuestAccessRequestDto;
import com.example.authservice.dto.auth.GuestAccessResponseDto;
import com.example.authservice.dto.auth.LoginRequestDto;
import com.example.authservice.dto.auth.LoginResponseDto;
import com.example.authservice.entities.UserStatus;
import com.example.authservice.entities.application.*;
import com.example.authservice.entities.enums.*;
import com.example.authservice.entities.role.Role;
import com.example.authservice.entities.role.RoleObject;
import com.example.authservice.entities.service.ExternalApi;
import com.example.authservice.entities.user.User;
import com.example.authservice.exception.AuthServiceMessageCode;
import com.example.authservice.filter.ClientDetailFilter;
import com.example.authservice.filter.RefreshTokenFilter;
import com.example.authservice.mapper.ClientMapper;
import com.example.authservice.mapper.RefreshTokenMapper;
import com.example.authservice.repo.*;
import com.example.authservice.service.iface.AuthService;
import com.example.authservice.service.iface.CryptoService;
import com.example.authservice.service.iface.UserService;
import com.example.authservice.utils.*;
import com.example.authservice.utils.cache.CacheRedisService;
import com.example.authservice.utils.exception.*;
import com.example.authservice.utils.permission.Permission;
import com.example.authservice.utils.response.DataPagingResponse;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

  private final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final CacheRedisService redisService;
  private final UserService userService;
  private final CryptoService cryptoService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final ClientUserRepository clientUserRepository;
  private final ApiKeyRepository apiKeyRepository;
  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ClientWhiteListRepository clientWhiteListRepository;
  private final RoleObjectRepository roleObjectRepository;
  private final RoleRepository roleRepository;

  private final RefreshTokenMapper refreshTokenMapper;
  private final ClientMapper clientMapper;
  private final ClientApiRepository clientApiRepository;
  private final ClientApiKeyRepository clientApiKeyRepository;

  @Value("${auth.code.prefix:auth:jwt:}")
  private String prefix;
  @Value("${auth.code.expire-time:1800}")
  private Long expireToken;
  @Value("${auth.token.access-token-validity:172800}")
  private Long accessTokenValidity;
  @Value("${auth.token.refresh-token-validity:7776000}")
  private Long refreshTokenValidity;
  @Value("${auth.token.access-token-length:64}")
  private Integer accessTokenLength;
  @Value("${auth.token.refresh-token-length:128}")
  private Integer refreshTokenLength;
  @Value("${auth.crypto.private-key}")
  private String privateKey;
  @Value("${auth.secret-key:Z#Rh]@t/ZZm8/&ws}")
  private String SECRET_KEY;
  @Value("${auth.api-key.expire-time:1800}")
  private Long expireApiKey;

  @Value("${service.role-owner.application:APPLICATION_OWNER_ROLE}")
  private String APPLICATION_OWNER_ROLE;

  public AuthServiceImpl(CacheRedisService redisService,
                         UserService userService,
                         CryptoService cryptoService,
                         PasswordEncoder passwordEncoder,
                         UserRepository userRepository,
                         ClientRepository clientRepository,
                         ClientUserRepository clientUserRepository,
                         ApiKeyRepository apiKeyRepository,
                         AccessTokenRepository accessTokenRepository,
                         RefreshTokenRepository refreshTokenRepository,
                         ClientWhiteListRepository clientWhiteListRepository,
                         RoleObjectRepository roleObjectRepository,
                         RoleRepository roleRepository,
                         RefreshTokenMapper refreshTokenMapper,
                         ClientMapper clientMapper,
                         ClientApiRepository clientApiRepository,
                         ClientApiKeyRepository clientApiKeyRepository) {
    this.redisService = redisService;
    this.userService = userService;
    this.cryptoService = cryptoService;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.clientRepository = clientRepository;
    this.clientUserRepository = clientUserRepository;
    this.apiKeyRepository = apiKeyRepository;
    this.accessTokenRepository = accessTokenRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.clientWhiteListRepository = clientWhiteListRepository;
    this.roleObjectRepository = roleObjectRepository;
    this.roleRepository = roleRepository;
    this.refreshTokenMapper = refreshTokenMapper;
    this.clientMapper = clientMapper;
    this.clientApiRepository = clientApiRepository;
    this.clientApiKeyRepository = clientApiKeyRepository;
  }

  @Override
  public LoginResponseDto login(HttpServletRequest request, LoginRequestDto loginRequestDto)
      throws ResourceNotFoundException, IdentifyBlankException, UnAuthorizedException, NoSuchAlgorithmException, CryptoException {

    if (loginRequestDto.getUserName() == null || loginRequestDto.getUserName().isBlank()) {
      throw new IdentifyBlankException("username not null",
          ServiceInfo.getId() + AuthServiceMessageCode.USERNAME_NOT_NULL);
    }
    if (loginRequestDto.getPassword() == null || loginRequestDto.getPassword().isBlank()) {
      throw new IdentifyBlankException("password not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PASSWORD_NOT_NULL);
    }
    String username = loginRequestDto.getUserName();
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new UnAuthorizedException("username incorrect",
          ServiceInfo.getId() + AuthServiceMessageCode.USERNAME_INCORRECT);
    }
    User user = userOptional.get();
    String password;
    try {
      password = cryptoService.rsaDecrypt(loginRequestDto.getPassword(), privateKey);
    } catch (Exception e) {
      throw new CryptoException("decrypt password error",
          ServiceInfo.getId() + AuthServiceMessageCode.DECRYPT_PASSWORD_ERROR);
    }
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new UnAuthorizedException("username or password incorrect",
          ServiceInfo.getId() + AuthServiceMessageCode.USERNAME_OR_PASS_INCORRECT);
    }
    if (user.getIsDeleted().equals(Boolean.TRUE) || user.getStatus()
        .equals(UserStatus.DEACTIVE)) {
      logger.info("user is deleted or de active");
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Set<Object> keys = redisService.keys(prefix + user.getUuid() + ":*");
    if (keys != null && !keys.isEmpty()) {
      Iterator<?> iter = keys.iterator();
      Object firstKey = iter.next();
      String token = firstKey.toString().split(":")[3];
      redisService.setExpireTime(firstKey.toString(), expireToken, TimeUnit.SECONDS);
      LoginResponseDto responseDto = new LoginResponseDto();
      responseDto.setToken(token);
      return responseDto;
    }
    Permission permission = userService.getPermissionsOfUser(user.getId());
    String jwtEncode = getJwt(user, null, permission, -1);

    MessageDigest salt = MessageDigest.getInstance("SHA-256");
    salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
    String code = Utils.bytesToHex(salt.digest());
    // Save token and clear cache after 30 minutes
    redisService.setValue(prefix + user.getUuid() + ':' + code, jwtEncode, expireToken);
    LoginResponseDto responseDto = new LoginResponseDto();
    responseDto.setToken(code);
    return responseDto;

  }

  String getJwt(User user, Map<String, Object> additionalParams, Permission permission, long exp) {
    Map<String, Object> data = new HashMap<>();
    data.put(KeyConstants.JSONKey.TYPE, "Bearer");
    data.put(KeyConstants.JSONKey.PREFERRED_USERNAME, user.getUsername());
    data.put(KeyConstants.JSONKey.USER_ID, user.getId());
    data.put(KeyConstants.JSONKey.EMAIL, user.getEmail());
    data.put(KeyConstants.JSONKey.NAME, user.getName());
    data.put(KeyConstants.JSONKey.PERMISSIONS, new Gson().toJson(permission));

    if (additionalParams != null) {
      data.putAll(additionalParams);
    }

    return new JWTGenerator(SECRET_KEY)
        .createJWT(String.valueOf(user.getId()), user.getName(), user.getUuid(), exp, data);
  }

  /**
   * @param request: HttpServletRequest
   * @return
   * @throws UnAuthorizedException
   */
  @Override
  public LoginResponseDto checkAuthenticate(HttpServletRequest request)
      throws UnAuthorizedException {
    //check public api flow
    JWTGenerator jwtGenerator = new JWTGenerator(SECRET_KEY);
    // Check access token flow
    logger.info("headers: {}", JsonUtils.toJson(RequestUtils.getRequestHeadersInMap(request)));

    // check api_key flow
    String apiKey = request.getHeader(KeyConstants.Headers.X_API_KEY);
    if (apiKey != null && !apiKey.isEmpty()) {
      Map<String, String> res = this.getJwtOfApiKey(apiKey);
      if (res != null && !res.isEmpty()) {
        String jwt = res.get(KeyConstants.RedisKey.JWT);
        if (!jwtGenerator.checkValidJWT(jwt)) {
          throw new UnAuthorizedException("jwt invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.JWT_INVALID);
        }
        // check IP
        String ipRequest = request.getHeader(Constants.AGENT);
        logger.info("ip request: {}", ipRequest);
        if (ipRequest == null || ipRequest.isBlank()) {
          throw new UnAuthorizedException("ip request is null or empty",
              ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
        }
        String ipWhiteList = res.get(KeyConstants.RedisKey.IP_WHITELIST);
        if (ipWhiteList == null || ipWhiteList.isBlank()) {
          logger.info("ip whitelist is null or empty");
          throw new UnAuthorizedException("client api key invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_API_KEY_INVALID);
        }
        List<String> listIpValid = Arrays
            .asList(JsonUtils.gson().fromJson(ipWhiteList, String[].class));
        if (listIpValid.isEmpty()) {
          logger.info("ip whitelist is empty");
          throw new UnAuthorizedException("client api key invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_API_KEY_INVALID);
        }
        if (!listIpValid.contains(ipRequest.trim())) {
          throw new UnAuthorizedException("ip request invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
        }
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setJwt(jwt);
        return responseDto;
      }
    }

    String accessToken = request.getHeader(KeyConstants.Headers.ACCESS_TOKEN);
    if (accessToken != null && !accessToken.isEmpty()) {
      String key = KeyConstants.RedisKey.AUTH_TOKEN + accessToken;
      if (redisService.exists(key)) {
        // check IP
        String ipRequest = request.getHeader(Constants.AGENT);
        logger.info("ip request: {}", ipRequest);
        if (ipRequest == null || ipRequest.isBlank()) {
          throw new UnAuthorizedException("ip request is null or empty",
              ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
        }
        String ipWhiteList = String.valueOf(redisService.hGet(key, KeyConstants.RedisKey.IP_WHITELIST));
        if (ipWhiteList == null || ipWhiteList.isBlank()) {
          logger.info("ip whitelist is null or empty");
          throw new UnAuthorizedException("token invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.ACCESS_TOKEN_INVALID);
        }
        List<String> listIpValid = Arrays
            .asList(JsonUtils.gson().fromJson(ipWhiteList, String[].class));
        if (listIpValid.isEmpty()) {
          logger.info("ip whitelist is empty");
          throw new UnAuthorizedException("token invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.ACCESS_TOKEN_INVALID);
        }
        if (!listIpValid.contains(ipRequest.trim())) {
          throw new UnAuthorizedException("ip request invalid",
              ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
        }
        boolean approved = Boolean
            .parseBoolean(redisService.hGet(key, KeyConstants.RedisKey.APPROVED).toString());
        long refreshTokenExp = Long
            .parseLong(redisService.hGet(key, KeyConstants.RedisKey.REFRESH_EXP).toString());
        if (approved && refreshTokenExp > System.currentTimeMillis()) {
          String jwtValue = String.valueOf(redisService.hGet(key, KeyConstants.RedisKey.JWT));
          if (jwtValue == null || jwtValue.isBlank() || !jwtGenerator.checkValidJWT(jwtValue)) {
            throw new UnAuthorizedException("jwt invalid",
                ServiceInfo.getId() + AuthServiceMessageCode.JWT_INVALID);
          }
          LoginResponseDto responseDto = new LoginResponseDto();
          responseDto.setJwt(jwtValue);
          return responseDto;
        }
      }
    }

    // basic token base flow
    String token = request.getHeader(KeyConstants.Headers.TOKEN);
    if (token == null || token.isBlank()) {
      throw new UnAuthorizedException("token is null or empty",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_NULL);
    }
    String pattern = "*:" + token;
    if (!redisService.hasKeyPattern(pattern)) {
      throw new UnAuthorizedException("token not found",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_NOT_FOUND);
    }
    Set<Object> keys = redisService.keys(pattern);
    Iterator<?> iter = keys.iterator();
    Object firstKey = iter.next();

    String jwtValue = redisService.getValue(firstKey.toString()).toString();
    if (jwtValue == null || jwtValue.isBlank()) {
      throw new UnAuthorizedException("jwt is null or empty",
          ServiceInfo.getId() + AuthServiceMessageCode.JWT_NOT_FOUND);
    }
    String jwtBody = EncodeUtils.decodeJWT(jwtValue);
    if (jwtBody == null || jwtBody.isBlank()) {
      logger.error("error when decode jwt");
      throw new UnAuthorizedException("jwt invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.JWT_INVALID);
    }
    JSONObject dataJson = new JSONObject(jwtBody);
    String userUuid = dataJson.get(KeyConstants.JSONKey.SUB).toString();

    String redisKey = prefix + userUuid + ":" + token;

    String jwtOrg = (String) redisService.getValue(redisKey);

    // update expireTime
    redisService.setExpireTime(redisKey, expireToken, TimeUnit.SECONDS);
    LoginResponseDto responseDto = new LoginResponseDto();
    responseDto.setToken(token);
    responseDto.setJwt(jwtOrg);
    return responseDto;
  }

  @Override
  public Boolean validateToken(HttpServletRequest request) throws UnAuthorizedException {
    String token = request.getHeader(Constants.TOKEN);
    if (token == null || token.isBlank()) {
      throw new UnAuthorizedException("token is null or empty",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_NULL);
    }
    String pattern = "*:" + token;
    if (!redisService.hasKeyPattern(pattern)) {
      throw new UnAuthorizedException("token not found",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_NOT_FOUND);
    }
    Set<Object> keys = redisService.keys(pattern);
    Iterator<?> iter = keys.iterator();
    Object firstKey = iter.next();

    String jwt = redisService.getValue(firstKey.toString()).toString();
    if (jwt == null || jwt.isBlank()) {
      throw new UnAuthorizedException("jwt is null or empty",
          ServiceInfo.getId() + AuthServiceMessageCode.JWT_NOT_FOUND);
    }
    String jwtBody = EncodeUtils.decodeJWT(jwt);
    if (jwtBody == null || jwtBody.isBlank()) {
      logger.info("error when decode jwt");
      throw new UnAuthorizedException("jwt invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.JWT_INVALID);
    }
    JSONObject dataJson = new JSONObject(jwtBody);
    String userUuid = dataJson.get(KeyConstants.JSONKey.SUB).toString();
    String redisKey = prefix + userUuid + ":" + token;
    // update expireTime
    redisService.setExpireTime(redisKey, expireToken, TimeUnit.SECONDS);
    return true;
  }

  /**
   * @param request
   */
  @Override
  public void logout(HttpServletRequest request) throws UnAuthorizedException, ServletException {
    String token = request.getHeader(Constants.TOKEN);
    if (token == null || token.isBlank()) {
      throw new UnAuthorizedException("token is null or empty",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_NULL);
    }
    String pattern = "*:" + token;
    if (redisService.hasKeyPattern(pattern)) {
      Set<Object> keys = redisService.keys(pattern);
      Iterator<?> iter = keys.iterator();
      Object firstKey = iter.next();

      String jwt = redisService.getValue(firstKey.toString()).toString();
      String jwtBody = EncodeUtils.decodeJWT(jwt);
      if (jwtBody == null) {
        logger.info("error when decode jwt");
        throw new UnAuthorizedException("jwt invalid",
            ServiceInfo.getId() + AuthServiceMessageCode.JWT_INVALID);
      }
      JSONObject dataJson = new JSONObject(jwtBody);
      String userUuid = dataJson.get(KeyConstants.JSONKey.SUB).toString();

      redisService.removePattern(prefix + userUuid + ":*");
    }
    request.logout();
  }

  @Override
  public LoginResponseDto reloadPermission(HttpServletRequest request)
      throws UnAuthorizedException, IdentifyBlankException {
    String token = request.getHeader(Constants.TOKEN);
    if (token == null || token.isBlank()) {
      throw new UnAuthorizedException(Constants.UNAUTHORIZED);
    }
    String pattern = "*:" + token;
    if (!redisService.hasKeyPattern(pattern)) {
      throw new UnAuthorizedException(Constants.UNAUTHORIZED);
    }
    Set<Object> keys = redisService.keys(pattern);
    Iterator<?> iter = keys.iterator();
    Object firstKey = iter.next();

    String jwtValue = redisService.getValue(firstKey.toString()).toString();
    if (jwtValue == null || jwtValue.isBlank()) {
      throw new UnAuthorizedException(Constants.UNAUTHORIZED);
    }
    String jwtBody = EncodeUtils.decodeJWT(jwtValue);
    if (jwtBody == null || jwtBody.isBlank()) {
      throw new UnAuthorizedException(Constants.UNAUTHORIZED);
    }
    JSONObject dataJson = new JSONObject(jwtBody);
    String userUuid = dataJson.get(KeyConstants.JSONKey.SUB).toString();

    String redisKey = prefix + userUuid + ":" + token;
    Optional<User> userOptional = userRepository.findByUuid(userUuid);
    if (userOptional.isPresent()) {
      Permission permission = userService.getPermissionsOfUser(userOptional.get().getId());
      String jwt = getJwt(userOptional.get(), null, permission, -1);
      // update expireTime
      redisService.setValue(redisKey, jwt);
      redisService.setExpireTime(redisKey, expireToken, TimeUnit.SECONDS);
      LoginResponseDto responseDto = new LoginResponseDto();
      responseDto.setToken(token);
      responseDto.setJwt(jwt);
      return responseDto;
    }

    String jwtOrg = (String) redisService.getValue(redisKey);
    // update expireTime
    redisService.setExpireTime(redisKey, expireToken, TimeUnit.SECONDS);
    LoginResponseDto responseDto = new LoginResponseDto();
    responseDto.setToken(token);
    responseDto.setJwt(jwtOrg);
    return responseDto;
  }

  @Override
  public void reloadPermission(List<Integer> userIdList) {
    List<User> userList = userRepository.findAllById(userIdList);
    List<String> uuidList = userList.stream().map(User::getUuid).collect(Collectors.toList());
    for (String id : uuidList) {
      String keyPattern = prefix + id + ":*";
      if (redisService.hasKeyPattern(keyPattern)) {
        redisService.removePattern(keyPattern);
      }
    }
  }

  @Override
  public String genAPIKey(int targetUserId)
      throws ResourceNotFoundException, NoSuchAlgorithmException {
    Optional<User> userOptional = userRepository.findById(targetUserId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      logger.info("user not found or deleted");
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ApiKey> listApiKey = apiKeyRepository.findByUserIdAndIsDeletedFalse(targetUserId);
    if (listApiKey.isPresent()) {
      ApiKey apiKey = listApiKey.get();
      return apiKey.getApiKey();
    }
    return generateNewAPIToken(userOptional.get());
  }

  String generateNewAPIToken(User user) throws NoSuchAlgorithmException {
    MessageDigest salt = MessageDigest.getInstance("SHA-256");
    salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
    String apiKey = Utils.bytesToHex(salt.digest());

    ApiKey key = new ApiKey();
    key.setApiKey(apiKey);
    key.setUser(user);
    key.setDeleted(false);
    ApiKey res = apiKeyRepository.save(key);
    return res.getApiKey();
  }

  private List<String> generateClientInfo() {
    List<String> clientInfo = new ArrayList<>();
    byte[] bytes = new byte[10];
    new Random().nextBytes(bytes);
    String clientId = UUID.randomUUID().toString();
    String clientSecret = Utils.bytesToHex(bytes);
    clientInfo.add(clientId);
    clientInfo.add(clientSecret);
    return clientInfo;
  }

  @Override
  public String getExistAPIKey(int userId) throws ResourceNotFoundException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ApiKey> listApiKey = apiKeyRepository.findByUserIdAndIsDeletedFalse(userId);
    if (listApiKey.isEmpty()) {
      return null;
    }
    ApiKey apiKey = listApiKey.get();
    return apiKey.getApiKey();
  }

  @Override
  public String reloadAPIKey(int userId)
      throws ResourceNotFoundException, NoSuchAlgorithmException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      logger.info("user not found or deleted");
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByUserIdAndIsDeletedFalse(userId);
    if (apiKeyOptional.isPresent()) {
      ApiKey apiKey = apiKeyOptional.get();
      apiKey.setDeleted(Boolean.TRUE);
      apiKeyRepository.save(apiKey);
    }
    return generateNewAPIToken(userOptional.get());
  }

  @Override
  @Transactional
  public Client addClient(ClientRequestDto clientRequestDto, Integer creatorId)
      throws IdentifyBlankException, DuplicateEntityException, ResourceNotFoundException {
    if (clientRequestDto.getName() == null || clientRequestDto.getName().isEmpty()) {
      throw new IdentifyBlankException("client name not null",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_NAME_NOT_NULL);
    }
    if (clientRequestDto.getAuthType() == null) {
      throw new IdentifyBlankException("auth type not null",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_AUTH_TYPE_NOT_NULL);
    }
//    Optional<Client> optional = clientRepository
//        .findByNameAndIsDeletedFalse(clientRequestDto.getName());
//    if (optional.isPresent()) {
//      throw new DuplicateEntityException("client already exist",
//          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_ALREADY_EXIST);
//    }
    Optional<User> userOptional = userRepository.findById(creatorId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user not found", ServiceInfo.getId() +
          AuthServiceMessageCode.USER_NOT_FOUND);
    }
    Client client = new Client();
    List<String> clientInfo = generateClientInfo();
    client.setName(clientRequestDto.getName());
    client.setClientId(clientInfo.get(0));
    client.setClientSecret(clientInfo.get(1));
    client.setDeleted(Boolean.FALSE);
    client.setCreatorUser(userOptional.get());
    client.setDescription(clientRequestDto.getDescription());
    client.setStatus(ClientStatus.ACTIVE);
    client.setOwner(userOptional.get());
    client.setAuthType(clientRequestDto.getAuthType());
    if (clientRequestDto.getApproveRequire() != null) {
      client.setApproveRequire(clientRequestDto.getApproveRequire());
    }
    if (clientRequestDto.getShareToken() != null) {
      client.setShareToken(clientRequestDto.getShareToken());
    }

    Client clientRes = clientRepository.save(client);
    addRoleOwnerApplication(clientRes.getId(), userOptional.get());
    return clientRes;
  }

  void addRoleOwnerApplication(Integer objectId, User user) {
    Optional<Role> roleOptional = roleRepository
        .findByCodeAndIsDeletedFalse(APPLICATION_OWNER_ROLE);
    if (roleOptional.isEmpty()) {
      return;
    }
    Role role = roleOptional.get();
    Optional<RoleObject> optional = roleObjectRepository
        .findByServiceNameAndObjectIdAndUserIdAndRoleIdAndIsDeletedFalse(
            PermissionObjectCode.APPLICATION, objectId, user.getId(), role.getId());
    if (optional.isEmpty()) {
      RoleObject roleObject = new RoleObject();
      roleObject.setServiceName(PermissionObjectCode.APPLICATION);
      roleObject.setObjectId(objectId);
      roleObject.setUser(user);
      roleObject.setRole(role);
      roleObject.setCreatorUserId(user.getId());
      roleObject.setIsDeleted(Boolean.FALSE);
      roleObjectRepository.save(roleObject);
    }
  }

  @Override
  public DataPagingResponse<ClientResponseDto> getAllClient(Integer userId, Integer page,
                                                            Integer limit, String search, ClientStatus status, String sort, Boolean isGetAll) {
    Specification<Client> filter;
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    if (!isGetAll) {
      List<Integer> clientIdList = clientRepository.findAllIdByOwnerId(userId);
      List<Integer> clientIdUserJoin = clientUserRepository.findListClientIdByUserId(userId);
      clientIdList.addAll(clientIdUserJoin);
      filter = new ClientDetailFilter()
          .filter(new HashSet<>(clientIdList), search, status, sortMap, false);
    } else {
      filter = new ClientDetailFilter().filter(null, search, status, sortMap, false);
    }
    Page<Client> clientsPage = clientRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<Client> users = clientsPage.getContent();
    List<ClientResponseDto> clientDetails = users.stream().map(clientMapper::getClient)
        .collect(Collectors.toList());
    clientDetails.forEach(it -> {
      it.setClientId(formatClientId(it.getClientId()));
      it.setClientSecret(formatClientSecret(it.getClientSecret()));
    });
    DataPagingResponse<ClientResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(clientDetails);
    dataPagingResponses.setTotalPage(clientsPage.getTotalPages());
    dataPagingResponses.setNum(clientsPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  String formatClientId(String clientId) {
    return clientId.substring(0, 15) + "...";
  }

  String formatClientSecret(String secret) {
    return secret.substring(0, 5) + "..." + secret.substring(secret.length() - 5);
  }

  @Override
  public ClientDetailDto getClientById(Integer id) throws ResourceNotFoundException {
    Optional<Client> optional = clientRepository.findById(id);
    if (optional.isEmpty() || optional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = optional.get();
    ClientDetailDto dto = clientMapper.toDto(client);
    // find api active of client
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository
        .findApiKeyActiveOfClient(client.getId());
    apiKeyOptional.ifPresent(clientApiKey -> dto.setApiKey(clientApiKey.getApiKey()));
    return dto;
  }

  @Override
  @Transactional
  @Modifying
  public void updateClient(Integer id, UpdateClientDto dto)
      throws ResourceNotFoundException {
    Optional<Client> optional = clientRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("client not found",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_NOT_EXIST);
    }
    if (optional.get().getDeleted() || optional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = optional.get();
    client.setName(dto.getName());
    client.setDescription(dto.getDescription());
    client.setAuthType(dto.getAuthType());
    if (dto.getAuthType().equals(ClientAuthType.OAUTH)) {
      if (dto.getApproveRequire() != null) {
        client.setApproveRequire(dto.getApproveRequire());
      }
      if (dto.getShareToken() != null) {
        client.setShareToken(dto.getShareToken());
      }
    } else {
      client.setApproveRequire(true);
      client.setShareToken(false);
    }
    Client clientResp = clientRepository.save(client);

    if (clientResp.getAuthType().equals(ClientAuthType.API_KEY)) {
      // expire all refresh_token, access_token and remove on redis
      List<RefreshToken> refreshTokenList = refreshTokenRepository
          .findAllByClientIdAndIsDeletedFalse(clientResp.getId());
      refreshTokenList.forEach(it -> {
        it.setStatus(RefreshTokenStatus.EXPIRED);
      });
      refreshTokenRepository.saveAll(refreshTokenList);

      List<AccessToken> accessTokenList = accessTokenRepository
          .findAllByClientId(clientResp.getId());
      accessTokenList.forEach(it -> {
        it.setStatus(TokenStatus.EXPIRED);
        String key = KeyConstants.RedisKey.AUTH_TOKEN.concat(it.getToken()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      accessTokenRepository.saveAll(accessTokenList);
    } else {
      // remove all api_key and remove on redis
      List<ClientApiKey> apiKeyList = clientApiKeyRepository
          .findAllByClientIdAndStatusAndIsDeletedFalse(clientResp.getId(),
              ClientApiKeyStatus.ACTIVE);
      apiKeyList.forEach(it -> {
        it.setStatus(ClientApiKeyStatus.EXPIRED);
        String key = KeyConstants.RedisKey.AUTH_APP.concat(it.getApiKey()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      clientApiKeyRepository.saveAll(apiKeyList);
    }

  }

  @Override
  public void deleteClient(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Client> optional = clientRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (optional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("Client has been deleted",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = optional.get();
    client.setDeleted(Boolean.TRUE);
    Client clientResp = clientRepository.save(client);
    // expire all refresh_token, access_token or api_key
    this.expireTokenOfClient(clientResp);
    this.expireApiKeyOfClient(clientResp);
  }

  @Override
  public Client changeStatusClient(Integer clientId, ClientStatus status)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Client> optional = clientRepository.findById(clientId);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("client not found",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (optional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("Client has been deleted",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = optional.get();
    client.setStatus(status);
    Client res = clientRepository.save(client);
    if (status.equals(ClientStatus.DEACTIVE)) {
      if (res.getAuthType().equals(ClientAuthType.OAUTH)) {
        this.expireTokenOfClient(res);
      } else {
        this.expireApiKeyOfClient(res);
      }
    }
    return res;
  }

  void expireTokenOfClient(Client client) {
    List<RefreshToken> refreshTokenList = refreshTokenRepository
        .findAllByClientIdAndIsDeletedFalse(client.getId());
    refreshTokenList.forEach(it -> {
      it.setStatus(RefreshTokenStatus.EXPIRED);
      it.setDeleted(Boolean.TRUE);
    });
    refreshTokenRepository.saveAll(refreshTokenList);
    List<Long> idList = refreshTokenList.stream().map(RefreshToken::getId)
        .collect(Collectors.toList());
    List<AccessToken> listAccessTokenValid = accessTokenRepository
        .findAllByRefreshTokenIdInAndExpireTimeAfter(idList, System.currentTimeMillis());
    // set all access_token expired
    if (!listAccessTokenValid.isEmpty()) {
      listAccessTokenValid.forEach(it -> {
        it.setExpireTime(System.currentTimeMillis());
        String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      accessTokenRepository.saveAll(listAccessTokenValid);
    }
  }

  void expireApiKeyOfClient(Client client) {
    List<ClientApiKey> apiKeyList = clientApiKeyRepository
        .findAllByClientIdAndStatusAndIsDeletedFalse(client.getId(), ClientApiKeyStatus.ACTIVE);
    apiKeyList.forEach(it -> {
      it.setStatus(ClientApiKeyStatus.EXPIRED);
      String key = KeyConstants.RedisKey.AUTH_APP.concat(it.getApiKey()).trim();
      if (redisService.exists(key)) {
        redisService.remove(key);
      }
    });
    clientApiKeyRepository.saveAll(apiKeyList);
  }

  @Override
  public void addIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = clientOptional.get();
    List<ClientWhiteList> list = new ArrayList<>();
    for (String ip : ipList) {
      Optional<ClientWhiteList> optional = clientWhiteListRepository
          .findByIpAndClientIdAndIsDeletedFalse(ip, clientId);
      if (optional.isPresent()) {
        continue;
      }

      ClientWhiteList clientWhiteList = new ClientWhiteList();
      clientWhiteList.setClient(client);
      clientWhiteList.setIp(ip);
      clientWhiteList.setDeleted(Boolean.FALSE);
      list.add(clientWhiteList);
    }
    clientWhiteListRepository.saveAll(list);

    // reload JWT of api_key
    this.reloadJwtOfApiKey(clientId);
  }

  @Override
  public void removeIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException {
    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    List<ClientWhiteList> list = new ArrayList<>();
    for (String ip : ipList) {
      Optional<ClientWhiteList> optional = clientWhiteListRepository
          .findByIpAndClientIdAndIsDeletedFalse(ip, clientId);
      if (optional.isEmpty()) {
        continue;
      }
      ClientWhiteList clientWhiteList = optional.get();
      clientWhiteList.setDeleted(Boolean.TRUE);
      list.add(clientWhiteList);
    }
    clientWhiteListRepository.saveAll(list);
    // reload JWT of api_key
    this.reloadJwtOfApiKey(clientId);
  }

  void reloadJwtOfApiKey(Integer clientId) {
    // delete cache
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository
        .findApiKeyActiveOfClient(clientId);
    if (apiKeyOptional.isEmpty()) {
      return;
    }
    ClientApiKey apiKey = apiKeyOptional.get();
    String key = KeyConstants.RedisKey.AUTH_APP.concat(apiKey.getApiKey());
    if (redisService.exists(key)) {
      redisService.remove(key);
    }
  }

  @Override
  public ClientWhiteListResponseDto getAllIpOfClient(Integer userId, Integer clientId)
      throws ResourceNotFoundException {

    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }

    Optional<Client> clientOptional = clientRepository.findById(clientId);
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    Client client = clientOptional.get();
    List<ClientWhiteList> listIp = clientWhiteListRepository
        .findAllByClientAndIsDeletedFalse(client);
    List<String> ips = listIp.stream().map(ClientWhiteList::getIp).collect(Collectors.toList());
    ClientWhiteListResponseDto responseDto = new ClientWhiteListResponseDto();
    responseDto.setClientId(clientId);
    responseDto.setClientName(client.getName());
    responseDto.setIpList(ips);
    return responseDto;
  }

  @Override
  public Map<String, Object> getToken(HttpServletRequest request, TokenRequestDto tokenRequestDto)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException, UnAuthorizedException {
    // check client info
    if (tokenRequestDto.getClientId() == null || tokenRequestDto.getClientId().isEmpty()) {
      throw new IdentifyBlankException("client id invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_ID_INVALID);
    }
    if (tokenRequestDto.getClientSecret() == null || tokenRequestDto.getClientSecret().isEmpty()) {
      throw new IdentifyBlankException("client secret invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_SECRET_INVALID);
    }
    Optional<Client> optional = clientRepository
        .findByClientId(tokenRequestDto.getClientId());
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("client not exist",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_NOT_EXIST);
    }
    Client client = optional.get();
    if (!client.getClientSecret().equals(tokenRequestDto.getClientSecret())) {
      throw new OperationNotImplementException("client secret invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_SECRET_INVALID);
    }
    if (client.getStatus() == null || client.getStatus().equals(ClientStatus.DEACTIVE)) {
      logger.info("client invalid or client deactived");
      throw new OperationNotImplementException("client invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_DEACTIVED);
    }

    // check apiKey is valid
    Optional<ApiKey> apiKeyOptional = apiKeyRepository
        .findByApiKey(tokenRequestDto.getApiKey());
    if (apiKeyOptional.isEmpty()) {
      throw new OperationNotImplementException("api token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.API_KEY_INVALID);
    }
    ApiKey apiKey = apiKeyOptional.get();
    // check IP is valid
    String agentIP = request.getHeader(Constants.AGENT);
    logger.info("ip request: {}", agentIP);
    if (agentIP == null || agentIP.isEmpty()) {
      throw new UnAuthorizedException("Ip request invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    List<String> listIpOfClient = findListIpOfClient(client);
    if (listIpOfClient == null || listIpOfClient.isEmpty()) {
      throw new UnAuthorizedException("ip whitelist null",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    if (!listIpOfClient.contains(agentIP)) {
      throw new UnAuthorizedException("Ip request invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
    }
    // if refresh_token already exist, delete it
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository
        .findByClientIdAndApiKeyIdAndIpAndIsDeletedFalse(client.getId(), apiKey.getId(), agentIP);
    if (refreshTokenOptional.isPresent()) {
      RefreshToken refreshToken = refreshTokenOptional.get();
      refreshToken.setStatus(RefreshTokenStatus.EXPIRED);
      refreshToken.setDeleted(Boolean.TRUE);
      RefreshToken refreshTokenResp = refreshTokenRepository.save(refreshToken);
      List<AccessToken> accessTokenList = accessTokenRepository
          .findAllByRefreshTokenIdAndIsDeletedFalse(refreshTokenResp.getId());
      if (!accessTokenList.isEmpty()) {
        accessTokenList.forEach(it -> {
          it.setIsDeleted(Boolean.TRUE);
          it.setStatus(TokenStatus.EXPIRED);
          String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
          if (redisService.exists(key)) {
            redisService.remove(key);
          }
        });
        accessTokenRepository.saveAll(accessTokenList);
      }
    }
    long currentTime = System.currentTimeMillis();

    String refreshToken = getRefreshToken();
    RefreshToken refToken = new RefreshToken();
    refToken.setExpireTime(currentTime + refreshTokenValidity * 1000);
    refToken.setToken(refreshToken);
    refToken.setClientId(client.getId());
    refToken.setClient(client);
    refToken.setApiKey(apiKey);
    refToken.setDeleted(Boolean.FALSE);
    refToken.setIp(agentIP);
    if (client.getApproveRequire() != null && client.getApproveRequire().equals(Boolean.FALSE)) {
      refToken.setApproved(Boolean.TRUE);
      refToken.setStatus(RefreshTokenStatus.ACTIVE);
    } else {
      refToken.setApproved(Boolean.FALSE);
      refToken.setStatus(RefreshTokenStatus.PENDING);
    }
    RefreshToken refreshTokenResp = refreshTokenRepository.save(refToken);

    TokenStatus tokenStatus;
    if (client.getApproveRequire() != null && client.getApproveRequire().equals(Boolean.FALSE)) {
      tokenStatus = TokenStatus.ACTIVE;
    } else {
      tokenStatus = TokenStatus.PENDING;
    }
    List<String> ipList;
    if (client.getShareToken() != null && client.getShareToken().equals(Boolean.TRUE)) {
      ipList = listIpOfClient;
    } else {
      ipList = Collections.singletonList(agentIP);
    }
    AccessToken accessTokenResp = generateAccessToken(apiKey, refreshTokenResp, currentTime,
        tokenStatus, ipList);

    Map<String, Object> map = new HashMap<>();
    map.put(KeyConstants.JSONKey.ACCESS_TOKEN, accessTokenResp.getToken());
    map.put(KeyConstants.JSONKey.ACCESS_TOKEN_EXP, accessTokenResp.getExpireTime());
    map.put(KeyConstants.JSONKey.REFRESH_TOKEN, refreshTokenResp.getToken());
    map.put(KeyConstants.JSONKey.REFRESH_TOKEN_EXP, refreshTokenResp.getExpireTime());
    return map;
  }

  final String getAccessToken() {
    GenerateUniqueKey accessTokenHash = new GenerateUniqueKey(UUID.randomUUID().toString(),
        accessTokenLength);
    return accessTokenHash.encode(accessTokenRepository.count());
  }

  final String getRefreshToken() {
    GenerateUniqueKey refreshTokenHash = new GenerateUniqueKey(UUID.randomUUID().toString(),
        refreshTokenLength);

    return refreshTokenHash.encode(refreshTokenRepository.count());
  }

  final List<String> findListIpOfClient(Client client) {
    List<ClientWhiteList> clientWhiteLists = clientWhiteListRepository
        .findAllByClientAndIsDeletedFalse(client);

    return clientWhiteLists.stream().map(ClientWhiteList::getIp).collect(Collectors.toList());
  }

  void formatListToken(List<RefreshTokenDto> tokens) {
    if (tokens.size() == 0) {
      return;
    }
    tokens.forEach(it -> {
      it.setToken(it.getToken().substring(0, 15) + "...");
      it.setClientId(formatClientId(it.getClientId()));
      it.setClientSecret(formatClientSecret(it.getClientSecret()));
    });
  }

  @Override
  public void approveToken(HttpServletRequest request, Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User user = this.checkValidUser(userId);
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshTokenId);
    if (refreshTokenOptional.isEmpty()) {
      throw new ResourceNotFoundException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    RefreshToken refreshToken = refreshTokenOptional.get();
    refreshToken.setApproved(Boolean.TRUE);
    refreshToken.setStatus(RefreshTokenStatus.ACTIVE);
    RefreshToken refreshTokenResp = refreshTokenRepository.save(refreshToken);
    List<AccessToken> accessTokenList = accessTokenRepository
        .findAllByRefreshTokenIdAndIsDeletedFalse(refreshTokenResp.getId());
    if (!accessTokenList.isEmpty()) {
      accessTokenList.forEach(it -> {
        String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
        if (!redisService.exists(key)) {
          return;
        }
        it.setStatus(TokenStatus.ACTIVE);
        it.setUpdaterUser(user);
        redisService.hSet(key, KeyConstants.RedisKey.APPROVED, String.valueOf(true));
      });
    }
    accessTokenRepository.saveAll(accessTokenList);
  }

  @Override
  public void unApproveToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User user = this.checkValidUser(userId);
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshTokenId);
    if (refreshTokenOptional.isEmpty()) {
      throw new ResourceNotFoundException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    RefreshToken refreshToken = refreshTokenOptional.get();
    refreshToken.setApproved(Boolean.FALSE);
    refreshToken.setDeleted(Boolean.TRUE);
    refreshToken.setStatus(RefreshTokenStatus.EXPIRED);
    RefreshToken refreshTokenResp = refreshTokenRepository.save(refreshToken);
    List<AccessToken> accessTokenList = accessTokenRepository
        .findAllByRefreshTokenIdAndIsDeletedFalse(refreshTokenResp.getId());
    if (!accessTokenList.isEmpty()) {
      accessTokenList.forEach(it -> {
        it.setIsDeleted(Boolean.TRUE);
        it.setStatus(TokenStatus.REJECTED);
        it.setDeleterUser(user);
        String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      accessTokenRepository.saveAll(accessTokenList);
    }
  }

  @Override
  public void changeTokenStatus(Long refreshTokenId, Integer userId, RefreshTokenStatus status)
      throws ResourceNotFoundException, OperationNotImplementException {
    if (!status.equals(RefreshTokenStatus.DEACTIVE) && !status.equals(RefreshTokenStatus.ACTIVE)) {
      throw new OperationNotImplementException("status invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.TOKEN_STATUS_INVALID);
    }
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshTokenId);
    if (refreshTokenOptional.isEmpty()) {
      throw new ResourceNotFoundException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    RefreshToken refreshToken = refreshTokenOptional.get();
    refreshToken.setStatus(status);
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  public void deleteToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User user = this.checkValidUser(userId);
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(refreshTokenId);
    if (refreshTokenOptional.isEmpty()) {
      throw new ResourceNotFoundException("refresh not found",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    RefreshToken refreshToken = refreshTokenOptional.get();
    refreshToken.setDeleted(Boolean.TRUE);
    refreshTokenRepository.save(refreshToken);

    List<AccessToken> accessTokenList = accessTokenRepository
        .findAllByRefreshTokenIdAndIsDeletedFalse(refreshToken.getId());
    if (!accessTokenList.isEmpty()) {
      accessTokenList.forEach(it -> {
        it.setIsDeleted(Boolean.TRUE);
        it.setStatus(TokenStatus.EXPIRED);
        it.setDeleterUser(user);
        String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      accessTokenRepository.saveAll(accessTokenList);
    }
  }

  @Override
  public Map<String, Object> getAccessTokenFromRefreshToken(HttpServletRequest request,
      String token)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException, UnAuthorizedException {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);
    if (optionalRefreshToken.isEmpty()) {
      throw new ResourceNotFoundException("refresh token not found",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_NOT_FOUND);
    }
    RefreshToken refreshToken = optionalRefreshToken.get();
    if (refreshToken.getApproved().equals(Boolean.FALSE)) {
      throw new OperationNotImplementException("refresh token un approved",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_UN_APPROVED);
    }
    if (refreshToken.getExpireTime() <= System.currentTimeMillis()) {
      throw new OperationNotImplementException("refresh token expired",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_EXPIRED);
    }

    Optional<Client> clientOptional = clientRepository.findById(refreshToken.getClientId());
    if (clientOptional.isEmpty() || clientOptional.get().getDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    Client client = clientOptional.get();
    if (client.getStatus() == null || client.getStatus().equals(ClientStatus.DEACTIVE)) {
      throw new OperationNotImplementException("client deactived",
          ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_DEACTIVED);
    }
    // check IP is valid
    String agentIP = request.getHeader(Constants.AGENT);
    List<String> listIp;
    if (client.getShareToken() != null && client.getShareToken().equals(Boolean.TRUE)) {
      List<String> listIpOfClient = findListIpOfClient(client);
      if (listIpOfClient == null || listIpOfClient.isEmpty()) {
        throw new UnAuthorizedException("ip whitelist null",
            ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
      }
      if (!listIpOfClient.contains(agentIP)) {
        throw new UnAuthorizedException("Ip request invalid",
            ServiceInfo.getId() + AuthServiceMessageCode.CLIENT_INVALID);
      }
      listIp = listIpOfClient;
    } else {
      String ipValid = refreshToken.getIp();
      if (ipValid == null || ipValid.isBlank()) {
        throw new UnAuthorizedException("Ip of refresh_token invalid",
            ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
      }
      if (agentIP == null || agentIP.isBlank()) {
        logger.info("ip request null or empty");
        throw new UnAuthorizedException("Ip request invalid",
            ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
      }
      if (!agentIP.trim().equals(ipValid.trim())) {
        throw new UnAuthorizedException("Ip request invalid",
            ServiceInfo.getId() + AuthServiceMessageCode.IP_INVALID);
      }
      listIp = Collections.singletonList(ipValid);
    }
    List<AccessToken> listAccessTokenValid = accessTokenRepository
        .findAllByRefreshTokenIdAndIsDeletedFalse(refreshToken.getId());

    // remove all current access_token and gen new access_token
    if (!listAccessTokenValid.isEmpty()) {
      listAccessTokenValid.forEach(it -> {
        it.setIsDeleted(Boolean.TRUE);
        it.setStatus(TokenStatus.EXPIRED);
        String key = (KeyConstants.RedisKey.AUTH_TOKEN + it.getToken()).trim();
        if (redisService.exists(key)) {
          redisService.remove(key);
        }
      });
      accessTokenRepository.saveAll(listAccessTokenValid);
    }

    Optional<ApiKey> apiKeyOptional = apiKeyRepository.findById(refreshToken.getApiKeyId());
    if (apiKeyOptional.isEmpty()) {
      throw new OperationNotImplementException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }
    ApiKey apiKey = apiKeyOptional.get();
    if (!apiKey.getUser().getStatus().equals(UserStatus.ACTIVE)) {
      throw new OperationNotImplementException("refresh token invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.REFRESH_TOKEN_INVALID);
    }

    AccessToken accessTokenResp = generateAccessToken(apiKey, refreshToken,
        System.currentTimeMillis(), TokenStatus.ACTIVE, listIp);

    Map<String, Object> map = new HashMap<>();
    map.put(KeyConstants.JSONKey.ACCESS_TOKEN, accessTokenResp.getToken());
    map.put(KeyConstants.JSONKey.ACCESS_TOKEN_EXP, accessTokenResp.getExpireTime());
    return map;
  }

  AccessToken generateAccessToken(ApiKey apiKey, RefreshToken refreshToken, Long createdTime,
      TokenStatus status, List<String> ipList)
      throws IdentifyBlankException {
    User user = apiKey.getUser();

    String accessToken = getAccessToken();
    AccessToken accToken = new AccessToken();
    accToken.setRefreshToken(refreshToken);
    accToken.setExpireTime(createdTime + accessTokenValidity * 1000);
    accToken.setToken(accessToken);
    accToken.setIsDeleted(Boolean.FALSE);
    accToken.setStatus(status);
    accToken.setCreatorUser(user);
    AccessToken accessTokenResp = accessTokenRepository.save(accToken);

    Map<String, Object> params = new HashMap<>();
    params.put(KeyConstants.JSONKey.CLIENT_ID, refreshToken.getClient().getClientId());
    Permission permission = userService.getPermissionsOfUser(user.getId());
    String jwt = getJwt(user, params, permission, accessTokenValidity * 1000);

    // save jwt, access_token to redis
    String keyPrefix = (KeyConstants.RedisKey.AUTH_TOKEN + accessTokenResp.getToken()).trim();
    Map<String, Object> data = new HashMap<>();
    data.put(KeyConstants.RedisKey.JWT, jwt);
    data.put(KeyConstants.RedisKey.APPROVED, String.valueOf(refreshToken.getApproved()));
    data.put(KeyConstants.RedisKey.REFRESH_TOKEN, refreshToken.getToken());
    data.put(KeyConstants.RedisKey.ACCESS_EXP, String.valueOf(accessTokenResp.getExpireTime()));
    data.put(KeyConstants.RedisKey.REFRESH_EXP, String.valueOf(refreshToken.getExpireTime()));
    data.put(KeyConstants.RedisKey.IP_WHITELIST, JsonUtils.toJson(ipList));
    redisService.hSetAll(keyPrefix, data);
    redisService.setExpireTime(keyPrefix, accessTokenValidity, TimeUnit.SECONDS);
    return accessTokenResp;
  }

  @Override
  public DataPagingResponse<RefreshTokenDto> getAllRefreshTokenOfUser(Integer userId, Integer page,
      Integer limit, String status, String sort) throws ResourceNotFoundException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("user invalid",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_INVALID);
    }
    Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByUserIdAndIsDeletedFalse(userId);
    if (apiKeyOptional.isEmpty()) {
      return null;
    }
    ApiKey apiKey = apiKeyOptional.get();
    List<String> statusList = new ArrayList<>();
    if (status != null && !status.isEmpty()) {
      statusList = Arrays.asList(status.split(","));
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);
    PageRequest pageRequest = PageRequest.of(page - 1, limit);
    Page<RefreshToken> data = refreshTokenRepository
        .findAll(new RefreshTokenFilter().getByFilter(apiKey.getId(), statusList, map, false),
            pageRequest);

    List<RefreshTokenDto> tokens = data.stream().map(refreshTokenMapper::toDto)
        .collect(Collectors.toList());
    // format listToken
    formatListToken(tokens);
    DataPagingResponse<RefreshTokenDto> response = new DataPagingResponse<>();
    response.setTotalPage(data.getTotalPages());
    response.setNum(data.getTotalElements());
    response.setList(tokens);
    response.setCurrentPage(page);
    return response;
  }

  @Override
  public GuestAccessResponseDto guestAccess(GuestAccessRequestDto request) throws NoSuchAlgorithmException {
    return null;
  }

  @Override
  public void temporaryClose(HttpServletRequest request) throws ResourceNotFoundException {

  }

  @Override
  public void saveUserActivity(UserActivityRequestDto dto, HttpServletRequest request) throws UnknownHostException {

  }

  private User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepository.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("user not found",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_DEACTIVE);
    }
    return userOptionalById.get();
  }

  Map<String, String> getJwtOfApiKey(String apiKey) {
    String key = KeyConstants.RedisKey.AUTH_APP.concat(apiKey);
    if (redisService.exists(key)) {
      String jwt = redisService.hGet(key, KeyConstants.RedisKey.JWT).toString();
      String ipList = redisService.hGet(key, KeyConstants.RedisKey.IP_WHITELIST).toString();

      // check valid of jwt, if jwt expired => delete cache
      JWTGenerator jwtGenerator = new JWTGenerator(SECRET_KEY);
      if (jwtGenerator.checkValidJWT(jwt)) {
        Map<String, String> map = new HashMap<>();
        map.put(KeyConstants.RedisKey.JWT, jwt);
        map.put(KeyConstants.RedisKey.IP_WHITELIST, ipList);
        return map;
      } else {
        try {
          logger.info("JWT of x-api-key invalid >> remove jwt and create new");
          redisService.remove(key);
        } catch (Exception e) {
          logger.info("error when remove key redis: key = {}, reason: {}", key, e.getMessage());
        }
      }
//      try {
//        redisService.setExpireTime(key, expireApiKey, TimeUnit.SECONDS);
//      } catch (Exception e) {
//        logger.info("error when set expire key redis: key = {}, reason: {}", key, e.getMessage());
//      }

    }
    Optional<ClientApiKey> apiKeyOptional = clientApiKeyRepository
        .findByApiKeyAndIsDeletedFalse(apiKey);
    if (apiKeyOptional.isEmpty()) {
      return null;
    }
    ClientApiKey clientApiKey = apiKeyOptional.get();
    if (clientApiKey.getStatus().equals(ClientApiKeyStatus.EXPIRED)) {
      return null;
    }

    // get list api of application
    List<ClientApi> clientApiList = clientApiRepository
        .findAllByClientIdAndIsDeletedFalse(clientApiKey.getClientId());
    List<ExternalApi> apiList = clientApiList.stream().map(ClientApi::getApi)
        .filter(it -> !it.getIsDeleted() && it.getStatus().equals(ApiStatus.ACTIVE))
        .collect(Collectors.toList());
    List<String> apiCodeList = apiList.stream().map(ExternalApi::getCode)
        .collect(Collectors.toList());

    Permission permission = new Permission();
    permission.setGeneralPermissions(apiCodeList);
    User owner = clientApiKey.getClient().getOwner();
    String jwt = this.getJwt(owner, null, permission, -1);
    List<String> listIpOfClient = findListIpOfClient(clientApiKey.getClient());

    Map<String, Object> map = new HashMap<>();
    map.put(KeyConstants.RedisKey.CLIENT_ID, String.valueOf(clientApiKey.getClientId()));
    map.put(KeyConstants.RedisKey.JWT, jwt);
    map.put(KeyConstants.RedisKey.IP_WHITELIST, JsonUtils.toJson(listIpOfClient));
    try {
      redisService.hSetAll(key, map);
    } catch (Exception e) {
      logger.info("error when set value to redis: key={}, reason: {}", key, e.getMessage());
    }
    Map<String, String> res = new HashMap<>();
    res.put(KeyConstants.RedisKey.JWT, jwt);
    res.put(KeyConstants.RedisKey.IP_WHITELIST, JsonUtils.toJson(listIpOfClient));
    return res;
  }
}
