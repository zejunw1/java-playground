package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.AccountCreationRequest;
import com.personal.javaplayground.services.AccountService;
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
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @RequestMapping(value = "/create-account", produces = "application/json", method = RequestMethod.POST)
    public Account createAccountForUser(@Valid @RequestBody AccountCreationRequest accountCreationRequest) {
        return accountService.createAccountForUser(accountCreationRequest.email);
    }
}
