package com.personal.javaplayground.services;

import com.personal.javaplayground.daos.TransactionHistoryRepository;
import com.personal.javaplayground.models.TransactionHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public List<TransactionHistory> getTransactionHistoryForAccount(String account) {
        return transactionHistoryRepository.findTop10ByAccountOrderByDateDesc(account);
    }
}
