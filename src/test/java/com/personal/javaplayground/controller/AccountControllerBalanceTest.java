package com.personal.javaplayground.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.javaplayground.daos.AccountRepository;
import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.models.*;
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
public class AccountControllerBalanceTest {
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

    private User user;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(new User("ZW", "zew@test.com"));
    }

    @Test
    void shouldDepositAmountToAccount() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 0.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new DepositRequest(accountId, 100.0));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/deposit", request, Account.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body.getBalance()).isEqualTo(100.0);
        assertThat(body.getAccount()).isEqualTo(accountId);
        assertThat(accountRepository.findById(accountId).get().getBalance()).isEqualTo(100.0);
    }


    @Test
    void shouldReturnErrorWhenDepositNegativeAmount() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 0.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new DepositRequest(accountId, -100.0));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/deposit", request, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body).isEqualTo("{\"error\":\"Cannot validate the request, please check api-doc(v3/api-docs) for more information\"}");
    }

    @Test
    void shouldWithdrawExpectedAmountIfBalanceSufficient() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 120.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new WithdrawRequest(accountId, 100.0));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/withdraw", request, Account.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body.getBalance()).isEqualTo(20);
        assertThat(body.getAccount()).isEqualTo(accountId);
        assertThat(accountRepository.findById(accountId).get().getBalance()).isEqualTo(20.0);
    }


    @Test
    void shouldThrowErrorIfBalanceInsufficient() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 120.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new WithdrawRequest(accountId, 130.0));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/withdraw", request, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body).isEqualTo("{\"error\":\"Insufficient funds\"}");
    }

    @Test
    void shouldReturnErrorIfNoAccountFound() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 0.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(new DepositRequest(UUID.randomUUID().toString(), 100.0));
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/deposit", request, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        var body = Objects.requireNonNull(response.getBody());
        assertThat(body).isEqualTo("{\"error\":\"No value present\"}");
    }

}
