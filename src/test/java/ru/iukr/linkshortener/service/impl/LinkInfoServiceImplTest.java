package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.repository.impl.LinkInfoRepositoryImpl;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkInfoServiceImplTest {

    @Test
    void createLinkInfoTest() {
        LinkInfoRepository repository = new LinkInfoRepositoryImpl();
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link("https://habr.com/")
                .build();
        LinkInfoService service = new LinkInfoServiceImpl(repository);
        service.createLinkInfo(request);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void getByShortLinkTest() {
        LinkInfoRepository repository = new LinkInfoRepositoryImpl();
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link("https://habr.com/")
                .build();
        LinkInfoService service = new LinkInfoServiceImpl(repository);
        service.createLinkInfo(request);
        LinkInfo linkInfo = repository.findAll().stream().findFirst().get();
        assertEquals(service.getByShortLink(linkInfo.getShortLink()).getShortLink(), linkInfo.getShortLink());
    }

    @Test
    void findByFilterTest() {
        LinkInfoRepository repository = new LinkInfoRepositoryImpl();
        CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                .endTime(LocalDateTime.now().plusDays(1))
                .link("https://habr.com/")
                .build();
        LinkInfoService service = new LinkInfoServiceImpl(repository);
        service.createLinkInfo(request);
        assertEquals(1, service.findByFilter().size());
    }
}