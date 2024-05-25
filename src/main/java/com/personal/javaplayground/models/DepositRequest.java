package com.personal.javaplayground.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepositRequest {
    @NotNull
    public String account;

    @Positive
    @NotNull
    public Double amount;

    public DepositRequest(String account, Double amount) {
        this.account = account;
        this.amount = amount;
    }

    public DepositRequest() {
    }
}
