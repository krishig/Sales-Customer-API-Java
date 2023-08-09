package com.KrishiG.util;

import com.KrishiG.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private UserService userService;

    public Long getUserIdFromToken(Map<String, String> token) {

        Long userId = null;
        String jwtToken = token.get("authorization");
        if (jwtToken != null) {
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
                userId =  Long.parseLong(map.get("public_id").toString());
                boolean existingUser = userService.getUserById(userId);
                if(existingUser) {
                    return userId;
                } else {
                    throw new RuntimeException("User not found!");
                }
            } catch (JsonProcessingException jpe) {
                System.out.println(jpe);
            }
        } else {
            throw new RuntimeException("please pass the token!");
        }
        return userId;
    }
}