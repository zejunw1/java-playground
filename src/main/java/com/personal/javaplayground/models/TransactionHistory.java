package com.personal.javaplayground.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "transaction_history", indexes = {
        @Index(name = "idx_account", columnList = "account")
})
public class TransactionHistory {
    @Id
    private String transactionId;

    private String account;
    private Double amount;
    private String type;
    private Date date;

    public TransactionHistory(String transactionId, String account, Double amount, String type) {
        this.transactionId = transactionId;
        this.account = account;
        this.amount = amount;
        this.type = type;
        date = new Date();
    }

    public TransactionHistory() {

        date = new Date();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
