package com.robin.ws.webserviceexample.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robin.ws.webserviceexample.entity.UserEntity;
import com.robin.ws.webserviceexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class GoogleController {

    @Value("${client-id}")
    private String CLIENT_ID;
    @Value("${client-secret}")
    private String CLIENT_SECRET;

    final UserRepository userRepository;

    @Autowired
    public GoogleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/storeauthcode", method = RequestMethod.POST)
    public String storeauthcode(Authentication authentication, @RequestBody String code,
                                @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            return "Error, wrong headers";
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    "http://localhost:8080")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);

        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();

        UserEntity userEntity = userRepository.findUserByEmail(authentication.getName());

        userEntity.setAccesToken(accessToken);
        userEntity.setRefreshToken(refreshToken);
        userEntity.setExparationTime(expiresAt);
        userEntity.setgMail(email);


        userRepository.save(userEntity);

        return "OK";
    }

}
