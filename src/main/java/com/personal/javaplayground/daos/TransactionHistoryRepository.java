package com.personal.javaplayground.daos;

import com.personal.javaplayground.models.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {

    List<TransactionHistory> findByAccount(String account);

    List<TransactionHistory> findTop10ByAccountOrderByDateDesc(String userId);

}
