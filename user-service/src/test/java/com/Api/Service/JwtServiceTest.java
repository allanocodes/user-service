package com.Api.Service;



import com.Api.Exceptions.InvalidTokenException;
import com.Api.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtServiceTest {

    private JwtService jwtService;


    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();  // Initialize the object here
    }

  @Test
  public void testGenarateSecretKey() {
      SecretKey secretKey = jwtService.getKey();
      assertNotNull(secretKey);
      assertEquals("HmacSHA256",secretKey.getAlgorithm());
  }

  @Test
  public void testGenaratetoken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      String username = "allan";
      String token = jwtService.generateToken(username);
      assertNotNull(token);

  }
  @Test
  public void testParseToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      String username = "allan";
      String token = jwtService.generateToken(username);

      Claims claims = jwtService.getClaims(token);
      assertEquals(claims.getSubject(),"allan");
  }

  @Test
  public void testValidToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

      String username = "allan";
      String token = jwtService.generateToken(username);

      assertTrue(jwtService.isTokenExpired(token));

  }

  @Test
  public void testWhenTokenIsExpired(){
        LocalDateTime localDateTime =LocalDateTime.now().minusHours(1);
      Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .setExpiration(date)
                .signWith(jwtService.getKey())
                .setSubject("allan")
                .compact();
        assertThrows(InvalidTokenException.class,()->{
            jwtService.validateToken(token);
        });
  }

 @Test
  public void testWithWrongToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      String username = "allan";
      String token = jwtService.generateToken(username);

      String invalid = "eyJhbGciOiJIUzI1NiJ9." +
              "eyJyb2xlIjoiVVNFUiIsInN1YiI6ImFsbGFuayIsImV4cCI6MTc1MjUwOTg2Mn0." +
              "U_lLZ6T-wnsr2P02jMt1s-_fgpd17o9eGsvj8NsEPY4";
        assertThrows(InvalidTokenException.class,()->{
            jwtService.getClaims(invalid);
        });
  }








}