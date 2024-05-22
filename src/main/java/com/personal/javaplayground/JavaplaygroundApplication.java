package com.personal.javaplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.personal.javaplayground", "com.personal.javaplayground.controller"
        , "com.personal.javaplayground.services", "com.personal.javaplayground.daos"})
public class JavaplaygroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaplaygroundApplication.class, args);
    }
}
