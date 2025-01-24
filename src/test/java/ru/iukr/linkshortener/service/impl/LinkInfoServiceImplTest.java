package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class LinkInfoServiceImplTest {

    @Autowired
    private LinkInfoService linkInfoService;
    @Autowired
    private LinkInfoRepository repository;

    private final String link = "https://habr.com/";
    private final LocalDateTime endDate = LocalDateTime.now().plusDays(1);
    private final CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
            .endTime(endDate)
            .link(link)
            .active(true)
            .build();

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
    void createLinkInfoTest() {
        int sizeBeforeCreation = repository.findAll().size();
        linkInfoService.createLinkInfo(request);
        int sizeAfterCreation = repository.findAll().size();
        assertTrue(sizeAfterCreation > sizeBeforeCreation);
    }

    @Test
    void getByShortLinkTest() {
        createIfNotExists();
        LinkInfo createdLinkInfo = repository
                .findAll()
                .stream()
                .filter(linkInfo -> linkInfo.getLink().equals(link))
                .findFirst().get();
        assertEquals(linkInfoService.getByShortLink(createdLinkInfo.getShortLink()).getShortLink(), createdLinkInfo.getShortLink());
    }

    @Test
    void deleteByLinkIdTest() {
        createIfNotExists();
        int sizeOfLinkStorage = repository.findAll().size();
        UUID id = repository.findAll().getFirst().getId();
        linkInfoService.deleteByLinkId(id);
        int sizeOfLinkStorageAfterDeletion = repository.findAll().size();
        assertTrue(sizeOfLinkStorage > sizeOfLinkStorageAfterDeletion);
    }

    @Test
    void updateLinkInfoTest() {
        String description = "New Description";
        createIfNotExists();
        UUID id = repository.findAll().getFirst().getId();
        LinkInfoUpdateRequest linkInfoUpdate = LinkInfoUpdateRequest.builder()
                .id(String.valueOf(id))
                .description(description)
                .build();
        linkInfoService.updateLinkInfo(linkInfoUpdate);
        String foundDescription = repository
                .findAll()
                .stream()
                .filter(linkInfoResponse -> linkInfoResponse.getId().equals(id))
                .findFirst()
                .map(LinkInfo::getDescription).get();
        assertEquals(description, foundDescription);
    }

    @Test
    void findByFilterTest() {
        createIfNotExists();
        assertFalse(linkInfoService.findByFilter(FilterLinkInfoRequest.builder()
                .linkPart(link).build()).isEmpty());
    }

    private void createIfNotExists() {
        if (repository.findAll().isEmpty()) {
            linkInfoService.createLinkInfo(request);
        }
    }
}