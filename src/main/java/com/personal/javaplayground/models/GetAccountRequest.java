package com.personal.javaplayground.models;

import jakarta.validation.constraints.NotNull;

public class GetAccountRequest {
    @NotNull
    public String account;

    public GetAccountRequest(String account) {
        this.account = account;
    }

}
