package com.example.authservice.dto.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * @author bontk
 * @created_date 31/08/2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class LogRequestResponseDto {

  private Long id;
  private String apiUrl;
  private String body;
  private String token;
  private String method;
  private String nameUser;
  private String ip;
  private Date createdTime;
  private Date resTime;
  private Long responseTimeStamp;
  private Long requestTimeStamp;
  private Integer resHttpCode;
  private String resBody;
  private String headers;
  private String resHeaders;
  private boolean resStatus;
  private String resMessage;
  private String params;
  private String authType;
  private Integer resHttpStatus;
  private String resErrorCode;
  private Long totalTime;
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getNameUser() {
    return nameUser;
  }

  public void setNameUser(String nameUser) {
    this.nameUser = nameUser;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Date getResTime() {
    return resTime;
  }

  public void setResTime(Date resTime) {
    this.resTime = resTime;
  }

  public Long getResponseTimeStamp() {
    return responseTimeStamp;
  }

  public void setResponseTimeStamp(Long responseTimeStamp) {
    this.responseTimeStamp = responseTimeStamp;
  }

  public Long getRequestTimeStamp() {
    return requestTimeStamp;
  }

  public void setRequestTimeStamp(Long requestTimeStamp) {
    this.requestTimeStamp = requestTimeStamp;
  }

  public Integer getResHttpCode() {
    return resHttpCode;
  }

  public void setResHttpCode(Integer resHttpCode) {
    this.resHttpCode = resHttpCode;
  }

  public String getResBody() {
    return resBody;
  }

  public void setResBody(String resBody) {
    this.resBody = resBody;
  }

  public String getHeaders() {
    return headers;
  }

  public void setHeaders(String headers) {
    this.headers = headers;
  }

  public String getResHeaders() {
    return resHeaders;
  }

  public void setResHeaders(String resHeaders) {
    this.resHeaders = resHeaders;
  }

  public boolean isResStatus() {
    return resStatus;
  }

  public void setResStatus(boolean resStatus) {
    this.resStatus = resStatus;
  }

  public String getResMessage() {
    return resMessage;
  }

  public void setResMessage(String resMessage) {
    this.resMessage = resMessage;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getAuthType() {
    return authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public Integer getResHttpStatus() {
    return resHttpStatus;
  }

  public void setResHttpStatus(Integer resHttpStatus) {
    this.resHttpStatus = resHttpStatus;
  }

  public String getResErrorCode() {
    return resErrorCode;
  }

  public void setResErrorCode(String resErrorCode) {
    this.resErrorCode = resErrorCode;
  }

  public Long getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(Long totalTime) {
    this.totalTime = totalTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
