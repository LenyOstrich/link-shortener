package ru.iukr.linkshortener.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkInfoControllerTest {
    @Autowired
    LinkInfoController linkInfoController;
    @Autowired
    LinkInfoRepository repository;

    private final LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

    private final CreateLinkInfoRequest body = CreateLinkInfoRequest.builder()
            .link("test")
            .active(true)
            .endTime(tomorrow)
            .build();

    @Test
    public void testCreateLinkInfo() {
        CommonRequest<CreateLinkInfoRequest> commonRequest = new CommonRequest<>(body);
        CommonResponse<LinkInfoResponse> response =  linkInfoController.postCreateLinkInfo(commonRequest);
        assertEquals(response.getBody().getLink(), commonRequest.getBody().getLink());
    }

    @Test
    public void testUpdateLinkInfo() {
        CommonResponse<LinkInfoResponse> response =  linkInfoController.postCreateLinkInfo(new CommonRequest<>(body));
        LinkInfoUpdateRequest updateRequest = LinkInfoUpdateRequest.builder()
                .id(response.getBody().getId())
                .link("test2")
                .build();
        CommonResponse<LinkInfoResponse> updateResponse = linkInfoController.postUpdateLinkInfo(new CommonRequest<>(updateRequest));
        assertEquals("test2", updateResponse.getBody().getLink());

    }

    @Test
    public void testDeleteLink() {
        CommonResponse<LinkInfoResponse> response =  linkInfoController.postCreateLinkInfo(new CommonRequest<>(body));
        int numberOfLinks = repository.findAll().size();
        linkInfoController.deleteLinkInfo(response.getBody().getId());
        assertEquals(numberOfLinks - 1, repository.findAll().size());
    }
}