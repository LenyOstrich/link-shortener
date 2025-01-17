package ru.iukr.linkshortener.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfoResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShortLinkControllerTest {
    @Autowired
    ShortLinkController shortLinkController;
    @Autowired
    LinkInfoController linkInfoController;

    @BeforeAll
    public static void setUp() {
        PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("test_db")
                .withUsername("test_user")
                .withPassword("test_pass");
        postgresContainer.start();

        // Настройка URL подключения для Spring
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Test
    public void testGetByShortLink() {
        CreateLinkInfoRequest body = CreateLinkInfoRequest.builder()
                .link("test")
                .active(true)
                .endTime(LocalDateTime.now().plusDays(1))
                .build();
        CommonRequest<CreateLinkInfoRequest> commonRequest = new CommonRequest<>(body);
        CommonResponse<LinkInfoResponse> response =  linkInfoController.postCreateLinkInfo(commonRequest);
        ResponseEntity<String> responseEntity = shortLinkController.getByShortLink(response.getBody().getShortLink());
        assertEquals(HttpStatus.TEMPORARY_REDIRECT, responseEntity.getStatusCode());
        assertEquals(response.getBody().getLink(), responseEntity.getHeaders().get(HttpHeaders.LOCATION).getFirst());
    }

}