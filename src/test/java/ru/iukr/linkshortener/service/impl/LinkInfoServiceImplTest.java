package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
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

    @Test
    void createLinkInfoTest() {
        int sizeBeforeCreation = linkInfoService.findAll().size();
        linkInfoService.createLinkInfo(request);
        int sizeAfterCreation = linkInfoService.findAll().size();
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
    void findAllTest() {
        createIfNotExists();
        assertFalse(linkInfoService.findAll().isEmpty());
    }

    @Test
    void deleteByLinkIdTest() {
        createIfNotExists();
        int sizeOfLinkStorage = linkInfoService.findAll().size();
        UUID id = linkInfoService.findAll().getFirst().getId();
        linkInfoService.deleteByLinkId(id);
        int sizeOfLinkStorageAfterDeletion = linkInfoService.findAll().size();
        assertTrue(sizeOfLinkStorage > sizeOfLinkStorageAfterDeletion);
    }

    @Test
    void updateLinkInfoTest() {
        String description = "New Description";
        createIfNotExists();
        UUID id = linkInfoService.findAll().getFirst().getId();
        LinkInfoUpdateRequest linkInfoUpdate = LinkInfoUpdateRequest.builder()
                .id(String.valueOf(id))
                .description(description)
                .build();
        linkInfoService.updateLinkInfo(linkInfoUpdate);
        String foundDescription = linkInfoService
                .findAll()
                .stream()
                .filter(linkInfoResponse -> linkInfoResponse.getId().equals(id))
                .findFirst()
                .map(LinkInfoResponse::getDescription).get();
        assertEquals(description, foundDescription);
    }

    @Test
    void findByFilterTest() {
        createIfNotExists();
        assertFalse(linkInfoService.findByFilter(FilterLinkInfoRequest.builder()
                .linkPart(link).build()).isEmpty());
    }

    private void createIfNotExists() {
        if (linkInfoService.findAll().isEmpty()) {
            linkInfoService.createLinkInfo(request);
        }
    }
}