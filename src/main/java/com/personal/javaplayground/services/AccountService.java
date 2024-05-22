package com.personal.javaplayground.services;

import com.personal.javaplayground.daos.AccountRepository;
import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserService userService;
    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public Account createAccountForUser(String mailAddress) {
        var user = userService.getUser(mailAddress);
        if (user == null) {
            throw new RuntimeException("User with email " + mailAddress + " does not exist");// Could have used a custom exception
        }
        var accountId = UUID.randomUUID();
        var account = new Account(accountId.toString(),user.getEmail(),0.0);
        return accountRepository.save(account);
    }
}
