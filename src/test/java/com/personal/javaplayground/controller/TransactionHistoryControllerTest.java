package com.personal.javaplayground.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.javaplayground.daos.AccountRepository;
import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.models.Account;
import com.personal.javaplayground.models.TransactionHistory;
import com.personal.javaplayground.models.User;
import com.personal.javaplayground.models.WithdrawRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionHistoryControllerTest {
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
    void shouldGetLast10TransactionHistory() throws JsonProcessingException {
        var accountId = UUID.randomUUID().toString();
        accountRepository.save(new Account(accountId, user.getEmail(), 120.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String withdrawJson = objectMapper.writeValueAsString(new WithdrawRequest(accountId, 100.0));
        String depositJson = objectMapper.writeValueAsString(new WithdrawRequest(accountId, 20.0));
        HttpEntity<String> withdrawRequest = new HttpEntity<>(withdrawJson, headers);
        HttpEntity<String> depositRequest = new HttpEntity<>(depositJson, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/withdraw", withdrawRequest, Account.class);
        var account = restTemplate.postForEntity("http://localhost:" + port + "/api/deposit", depositRequest, Account.class);
        var transactionHistories = restTemplate.exchange("http://localhost:" + port + "/api/last-10-transaction-history?account=" + accountId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionHistory>>() {
                });

        assertThat(Objects.requireNonNull(account.getBody()).getBalance()).isEqualTo(40);

        var transactionHistoryBody = Objects.requireNonNull(transactionHistories.getBody());
        assertThat(transactionHistories.getStatusCode().value()).isEqualTo(200);
        assertThat(transactionHistoryBody.size()).isEqualTo(2);

        assertThat((transactionHistoryBody).getFirst().getAmount()).isEqualTo(20.0);
        assertThat((transactionHistoryBody).getFirst().getType()).isEqualTo("DEPOSIT");
        assertThat((transactionHistoryBody).getFirst().getAccount()).isEqualTo(accountId);
        assertThat((transactionHistoryBody).get(1).getAmount()).isEqualTo(100.0);
        assertThat((transactionHistoryBody).get(1).getType()).isEqualTo("WITHDRAW");
        assertThat((transactionHistoryBody).get(1).getAccount()).isEqualTo(accountId);
        assertTrue((transactionHistoryBody).getFirst().getDate().after((transactionHistoryBody).get(1).getDate()));
    }
}