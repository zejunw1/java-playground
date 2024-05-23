package com.personal.javaplayground.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class WithdrawRequest {
    @NotNull
    @Email
    public String account;

    @NotNull
    public Double amount;

}
