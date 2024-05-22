package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.User;
import com.personal.javaplayground.models.UserCreationRequest;
import com.personal.javaplayground.services.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;


@RestController
@AutoConfiguration
@RequestMapping("/v1")
public class UserCreationController {

    private final UserService userService;

    public UserCreationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/createUser", produces = "application/json", method = RequestMethod.POST)
    public User createUser(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        var user = new User(userCreationRequest.displayName, userCreationRequest.email, userCreationRequest.password);
        return userService.saveUser(user);
    }
}
