package com.personal.javaplayground.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreationRequest {
    @NotNull
    @Size(min = 12, max = 64)
    public String password;

    @NotNull
    @Email
    public String email;

    @NotNull
    @Size(min = 1, max = 64)
    public String displayName;

    public UserCreationRequest(String password, String email, String displayName) {
        this.password = password;
        this.email = email;
        this.displayName = displayName;
    }
}
