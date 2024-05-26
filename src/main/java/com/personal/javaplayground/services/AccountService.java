package com.personal.javaplayground.services;

import com.personal.javaplayground.daos.AccountRepository;
import com.personal.javaplayground.daos.TransactionHistoryRepository;
import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.DepositRequest;
import com.personal.javaplayground.models.TransactionHistory;
import com.personal.javaplayground.models.WithdrawRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final TransactionHistoryRepository transactionHistoryRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionHistoryRepository transactionHistoryRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    @Transactional
    public Account createAccountForUser(String mailAddress) {
        var user = userRepository.getUserWithLock(mailAddress);
        if (user.isEmpty()) {
            throw new RuntimeException("User with email " + mailAddress + " does not exist");// Could have used a custom exception
        }
        var accountId = UUID.randomUUID();
        var account = new Account(accountId.toString(), mailAddress, 0.0);
        return accountRepository.save(account);
    }

    public Account getAccount(String accountId) {
        var account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new RuntimeException("Account with id " + accountId + " does not exist");// Could have used a custom exception
        }
        return account.get();
    }

    @Transactional
    public Account deposit(DepositRequest depositRequest) {
        var account = accountRepository.findById(depositRequest.account)
                .orElseThrow();// Could have used a custom exception
        account.setBalance(account.getBalance() + depositRequest.amount);
        var updatedAccount = accountRepository.save(account);
        var transactionId = UUID.randomUUID().toString();
        transactionHistoryRepository.save(new TransactionHistory(transactionId, depositRequest.account, depositRequest.amount, "DEPOSIT"));
        return updatedAccount;
    }

    @Transactional
    public Account withdraw(WithdrawRequest withdrawRequest) {
        var account = accountRepository.findById(withdrawRequest.account)
                .orElseThrow();// Could have used a custom exception

        if (account.getBalance() < withdrawRequest.amount) {
            throw new RuntimeException("Insufficient funds");// Could have used a custom exception
        }
        account.setBalance(account.getBalance() - withdrawRequest.amount);
        var updatedAccount = accountRepository.save(account);
        transactionHistoryRepository.save(new TransactionHistory(UUID.randomUUID().toString(), withdrawRequest.account, withdrawRequest.amount, "WITHDRAW"));
        return updatedAccount;
    }
}
