package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.User;
import com.personal.javaplayground.models.UserCreationRequest;
import com.personal.javaplayground.services.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AutoConfiguration
public class UserCreationController {

    private final UserService userService;

    public UserCreationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/create-user", produces = "application/json", method = RequestMethod.POST)
    public User createUser(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        var user = new User(userCreationRequest.displayName, userCreationRequest.email);
        return userService.saveUser(user);
    }
}
