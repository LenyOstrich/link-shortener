package ru.iukr.linkshortener.service;

import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findAll();

    void deleteByLinkId(UUID id);

    LinkInfoResponse updateLinkInfo(LinkInfoUpdateRequest linkInfo);

    List<LinkInfoResponse> getFilteredLinkInfos(FilterLinkInfoRequest body);
}
