package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.TransactionHistory;
import com.personal.javaplayground.services.TransactionHistoryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AutoConfiguration
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @RequestMapping(value = "/last-10-transaction-history", produces = "application/json", method = RequestMethod.GET)
    public List<TransactionHistory> getTransactionHistories(@NotNull @RequestParam String account) {
        return transactionHistoryService.getTransactionHistoryForAccount(account);
    }
}
