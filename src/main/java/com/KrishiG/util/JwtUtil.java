package com.KrishiG.util;

import com.KrishiG.exception.JwtTokenException;
import com.KrishiG.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private UserService userService;

    public Long getUserIdFromToken(Map<String, String> token) {

        Long userId = null;
        String jwtToken = token.get("authorization");
        if (jwtToken != null && jwtToken!="") {
            try {
                String[] split_string = jwtToken.split("\\.");
                String base64EncodedHeader = split_string[0];
                String base64EncodedBody = split_string[1];
                String base64EncodedSignature = split_string[2];

                Base64 base64Url = new Base64(true);
                String header = new String(base64Url.decode(base64EncodedHeader));

                String body = new String(base64Url.decode(base64EncodedBody));
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(body, Map.class);
                Long i = Long.parseLong(map.get("exp").toString());
                if(isTokenExpire(i)){
                    throw new JwtTokenException("Please login again!");
                }
                userId =  Long.parseLong(map.get("public_id").toString());
                boolean existingUser = userService.getUserById(userId);
                if(existingUser) {
                    return userId;
                } else {
                    throw new JwtTokenException("User does not exist!");
                }
            } catch (JsonProcessingException jpe) {
                System.out.println(jpe);
            }
        } else {
            throw new JwtTokenException("Please pass the token!");
        }
        return userId;
    }

    private boolean isTokenExpire(Long date) {
        String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date (date*1000));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expDate = LocalDateTime.parse(dateStr, formatter);
        if(expDate.plusSeconds(1).isBefore(currentDateTime)) {
            return true;
        }
        return false;
    }
}