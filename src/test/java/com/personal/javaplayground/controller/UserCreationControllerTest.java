package com.personal.javaplayground.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.javaplayground.daos.UserRepository;
import com.personal.javaplayground.models.User;
import com.personal.javaplayground.models.UserCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserCreationControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnErrorIfNotProvidingCorrectMailAddress() throws JsonProcessingException {
        UserCreationRequest userCreationRequest = new UserCreationRequest("test@@@mail.com", "ZJ W");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(userCreationRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Act
        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/create-user",
                request,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("{\"error\":\"Cannot validate the request, please check api-doc(v3/api-docs) for more information\"}");
    }


    @Test
    void shouldReturnErrorIfNoEmailAddressProvided() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = "{\"displayName\":\"ZJ W\"}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Act
        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/create-user",
                request,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("{\"error\":\"Cannot validate the request, please check api-doc(v3/api-docs) for more information\"}");
    }

    @Test
    void shouldCreateTheUser() throws JsonProcessingException {
        UserCreationRequest userCreationRequest = new UserCreationRequest("test@mail.com", "ZJ W");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(userCreationRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Act
        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/create-user",
                request,
                User.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getEmail()).isEqualTo("test@mail.com");
        assertThat(response.getBody().getDisplayName()).isEqualTo("ZJ W");
    }

    @Test
    void shouldReturnErrorIfUserExists() throws JsonProcessingException {
        UserCreationRequest userCreationRequest = new UserCreationRequest("test@mail.com", "ZJ W");

        userRepository.save(new User(userCreationRequest.displayName, userCreationRequest.email));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(userCreationRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/create-user",
                request,
                String.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("{\"error\":\"User with email test@mail.com already exists\"}");
    }
}
