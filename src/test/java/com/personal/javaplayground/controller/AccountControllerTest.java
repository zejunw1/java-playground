package com.personal.javaplayground.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.javaplayground.daos.AccountRepository;
import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.AccountCreationRequest;
import com.personal.javaplayground.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAccountForUser() throws JsonProcessingException {
        var user = userRepository.save(new User("ZW", "test@mail.com"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new AccountCreationRequest(user.getEmail()));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/create-account", request, Account.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body.getEmail()).isEqualTo(user.getEmail());
        assertThat(body.getBalance()).isEqualTo(0.0);
        assertThat(body.getAccount()).isNotNull();
    }

    @Test
    void shouldReturnAccountBalance() {
        var user = userRepository.save(new User("ZW", "test@mail.com"));
        var accountId = UUID.randomUUID().toString();
        var account = accountRepository.save(new Account(accountId, user.getEmail(), 100.0));

        var response = restTemplate.getForEntity("http://localhost:" + port + "/api/get-account?account=" + accountId, Account.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body.getEmail()).isEqualTo(user.getEmail());
        assertThat(body.getBalance()).isEqualTo(100.0);
        assertThat(body.getAccount()).isEqualTo(accountId);
    }
}