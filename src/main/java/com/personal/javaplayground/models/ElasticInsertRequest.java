package com.personal.javaplayground.models;

import jakarta.validation.constraints.NotNull;

public class ElasticInsertRequest {
    @NotNull
    public String id;
    public String status;
    public String path;
}
