package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.AccountCreationRequest;
import com.personal.javaplayground.models.DepositRequest;
import com.personal.javaplayground.models.WithdrawRequest;
import com.personal.javaplayground.services.AccountService;
import com.personal.javaplayground.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AutoConfiguration
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/create-account", produces = "application/json", method = RequestMethod.POST)
    public Account createAccountForUser(@Valid @RequestBody AccountCreationRequest accountCreationRequest) {
        return accountService.createAccountForUser(accountCreationRequest.email);
    }

    @RequestMapping(value = "/get-account", produces = "application/json", method = RequestMethod.GET)
    public Account getAccount(@NotNull @RequestParam String account) {
        return accountService.getAccount(account);
    }

    @RequestMapping(value = "/deposit", produces = "application/json", method = RequestMethod.POST)
    public Account deposit(@Valid @RequestBody DepositRequest depositRequest) {
        return accountService.deposit(depositRequest);
    }


    @RequestMapping(value = "/withdraw", produces = "application/json", method = RequestMethod.POST)
    public Account deposit(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        return accountService.withdraw(withdrawRequest);
    }
}
