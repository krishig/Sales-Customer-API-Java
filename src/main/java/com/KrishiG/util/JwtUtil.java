package com.KrishiG.util;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.KrishiG.exception.JwtTokenException;
import com.KrishiG.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET = "heythisisthesecretkey";

    @Autowired
    private UserService userService;

    public Long getUserIdFromToken(Map<String, String> token) {

        Long userId = null;
        Date date = null;
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
                userId = Long.parseLong(map.get("public_id").toString());
                boolean existingUser = userService.getUserById(userId);
                if (existingUser) {
                    return userId;
                } else {
                    throw new JwtTokenException("User does not exist!");
                }
            } catch (JsonProcessingException jpe) {
                System.out.println(jpe);
            }
        }
             else{
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

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
}