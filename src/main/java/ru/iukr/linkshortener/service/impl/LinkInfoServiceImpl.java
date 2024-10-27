package ru.iukr.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.exception.NotFoundException;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.model.LinkInfoUpdateModel;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository repository;
    private final LinkInfoProperty property;

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(property.getShortLinkLength());
        LinkInfo linkInfo = LinkInfo.builder()
                .shortLink(shortLink)
                .link(linkInfoRequest.getLink())
                .endTime(linkInfoRequest.getEndTime())
                .description(linkInfoRequest.getDescription())
                .active(linkInfoRequest.getActive())
                .openingCount(0L)
                .build();
        LinkInfo savedLinkInfo = repository.save(linkInfo);
        return toResponse(savedLinkInfo);
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        return repository.findByShortLink(shortLink).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Не удалось найти сущность по короткой ссылке: " + shortLink));
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void deleteByLinkId(UUID id) {
        repository.deleteLink(id);
    }

    @Override
    public LinkInfoResponse updateLinkInfo(LinkInfoUpdateModel linkInfo) {
        return toResponse(repository.update(linkInfo));
    }

    private LinkInfoResponse toResponse(LinkInfo linkInfo) {
        return LinkInfoResponse.builder()
                .id(linkInfo.getId())
                .link(linkInfo.getLink())
                .shortLink(linkInfo.getShortLink())
                .endTime(linkInfo.getEndTime())
                .description(linkInfo.getDescription())
                .active(linkInfo.getActive())
                .openingCount(linkInfo.getOpeningCount())
                .build();
    }
}
