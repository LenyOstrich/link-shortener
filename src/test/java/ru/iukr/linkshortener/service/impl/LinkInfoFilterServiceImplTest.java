package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkInfoFilterServiceImplTest {
    @Autowired
    LinkInfoFilterServiceImpl linkInfoFilterService;

    @Test
    public void testApplyFilters() {
        LinkInfoResponse linkInfoResponse = LinkInfoResponse.builder()
                .link("https://test.com")
                .build();
        FilterLinkInfoRequest body = FilterLinkInfoRequest.builder()
                .linkPart("test")
                .build();
        assertTrue(linkInfoFilterService.applyFilters(linkInfoResponse, body));
    }
}