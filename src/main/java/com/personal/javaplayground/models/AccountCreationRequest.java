package com.personal.javaplayground.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AccountCreationRequest {
    @NotNull
    @Email
    public String email;

    public AccountCreationRequest(String email) {
        this.email = email;
    }


}
