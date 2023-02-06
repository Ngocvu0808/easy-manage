package com.example.authservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JWTGenerator {

  private final Logger logger = LoggerFactory.getLogger(JWTGenerator.class);

  private final String SECRET_KEY;

  public JWTGenerator(String secretKey) {
    this.SECRET_KEY = secretKey;
  }

  public String createJWT(String id, String issuer, String subject, long ttlMillis,
      Map<String, Object> data) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder builder = Jwts.builder()
        .setId(id)
        .setIssuedAt(now)
        .setSubject(subject)
        .setIssuer(issuer)
        .addClaims(data)
        .setHeaderParam("type", "JWT")
        .signWith(signatureAlgorithm, signingKey);

    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }

    return builder.compact();
  }

  public Claims decodeJWT(String jwt) {
    return Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(jwt).getBody();
  }

  public boolean checkValidJWT(String jwt) {
    if (jwt == null) {
      return false;
    }
    if (jwt.contains(" ") || jwt.contains("\n")) {
      return false;
    }
    String[] jwtArr = jwt.split("\\.");
    if (jwtArr.length != 3) {
      return false;
    }
    if (!Base64.isBase64(jwtArr[0]) || !Base64.isBase64(jwtArr[1])) {
      return false;
    }
    try {
      Jwts.parser()
          .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
          .parseClaimsJws(jwt).getBody();
      return true;
    } catch (Exception e) {
      logger.info("error when check valid jwt: {}", e.getMessage());
      return false;
    }
  }
}
