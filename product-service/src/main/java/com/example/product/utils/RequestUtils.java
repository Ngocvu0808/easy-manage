package com.example.product.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

  private static final String[] IP_HEADER_CANDIDATES = new String[]{"x-real-ip", "X-Forwarded-For",
      "x-real-ip", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR",
      "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
      "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

  public RequestUtils() {
  }

  public static String getClientIP(HttpServletRequest request) {
    String[] var1 = IP_HEADER_CANDIDATES;
    int var2 = var1.length;

    for (int var3 = 0; var3 < var2; ++var3) {
      String header = var1[var3];
      String ip = request.getHeader(header);
      if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip) && !ip.contains(",")) {
        return ip;
      }
    }

    return request.getRemoteAddr();
  }

  public static Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {
    Map<String, String> result = new HashMap();
    Enumeration<?> headerNames = request.getHeaderNames();

    while (headerNames.hasMoreElements()) {
      String key = (String) headerNames.nextElement();
      String value = request.getHeader(key);
      result.put(key, value);
    }

    return result;
  }
}
