package ru.iukr.linkshortener.service;

import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.UpdateRequest;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();

    void deleteByLinkId(UUID id);

    LinkInfoResponse updateLinkInfo(UpdateRequest linkInfo);
}
