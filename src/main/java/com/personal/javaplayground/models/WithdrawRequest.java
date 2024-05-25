package com.personal.javaplayground.models;

import jakarta.validation.constraints.NotNull;

public class WithdrawRequest {
    @NotNull
    public String account;

    @NotNull
    public Double amount;

    public WithdrawRequest(String account, Double amount) {
        this.account = account;
        this.amount = amount;
    }

    public WithdrawRequest() {
    }
}
