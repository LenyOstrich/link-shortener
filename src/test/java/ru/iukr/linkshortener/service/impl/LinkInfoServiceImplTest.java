package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.UpdateRequest;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.repository.impl.LinkInfoRepositoryImpl;
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
    @Autowired
    private LinkInfoProperty property;

    private final String link = "https://habr.com/";


    @Test
    void createLinkInfoTest() {
        int sizeBeforeCreation = linkInfoService.findByFilter().size();
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link(link)
                .build();
        linkInfoService.createLinkInfo(request);
        int sizeAfterCreation = linkInfoService.findByFilter().size();
        assertTrue(sizeAfterCreation > sizeBeforeCreation);
    }

    @Test
    void getByShortLinkTest() {
        LinkInfoRepository repository = new LinkInfoRepositoryImpl();
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link(link)
                .build();
        LinkInfoService service = new LinkInfoServiceImpl(repository, property);
        LinkInfoService serviceWithTime = new LogExecutionTimeLinkInfoServiceProxy(service);
        serviceWithTime.createLinkInfo(request);
        LinkInfo createdLinkInfo = repository.findAll().stream().filter(linkInfo -> linkInfo.getLink().equals(link)).findFirst().get();
        assertEquals(serviceWithTime.getByShortLink(createdLinkInfo.getShortLink()).getShortLink(), createdLinkInfo.getShortLink());
    }

    @Test
    void findByFilterTest() {
        LinkInfoService service = new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link(link)
                .build();
        service.createLinkInfo(request);
        assertFalse(service.findByFilter().isEmpty());
    }

    @Test
    void deleteByLinkIdTest() {
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link(link)
                .build();
        linkInfoService.createLinkInfo(request);
        int sizeOfLinkStorage = linkInfoService.findByFilter().size();
        UUID id = linkInfoService.findByFilter().get(0).getId();
        linkInfoService.deleteByLinkId(id);
        int sizeOfLinkStorageAfterDeletion = linkInfoService.findByFilter().size();
        assertTrue(sizeOfLinkStorage > sizeOfLinkStorageAfterDeletion);
    }

    @Test
    void updateLinkInfoTest() {
        String description = "New Description";
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link(link)
                .build();
        linkInfoService.createLinkInfo(request);
        UUID id = linkInfoService.findByFilter().get(0).getId();
        UpdateRequest linkInfoUpdate = UpdateRequest.builder()
                .id(id)
                .description(description)
                .build();
        linkInfoService.updateLinkInfo(linkInfoUpdate);
        String foundDescription = linkInfoService.findByFilter().stream().filter(linkInfoResponse -> linkInfoResponse.getId() == id).findFirst()
                .map(LinkInfoResponse::getDescription).get();
        assertEquals(description, foundDescription);
    }
}