package com.robin.ws.webserviceexample.controller;

import com.robin.ws.webserviceexample.dto.UserDto;
import com.robin.ws.webserviceexample.models.request.UserDetailsRequestModel;
import com.robin.ws.webserviceexample.models.response.UserResponseModel;
import com.robin.ws.webserviceexample.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/{id}", produces = {"application/json", "application/xml"}, method = RequestMethod.GET)
    public UserResponseModel getUser(@PathVariable String id) {
        UserResponseModel response = new UserResponseModel();

        UserDto userDto = userService.getUserById(id);
        BeanUtils.copyProperties(userDto, response);

        return response;
    }

    @RequestMapping(consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}, method = RequestMethod.POST)
    public UserResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserResponseModel returnValue = new UserResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);
        return returnValue;
    }

}
