package com.robin.ws.webserviceexample.service;

import com.robin.ws.webserviceexample.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserById(String id);
}
