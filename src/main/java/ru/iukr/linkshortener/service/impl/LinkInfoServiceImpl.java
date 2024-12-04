package ru.iukr.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.iukr.linkshortener.annotation.LogExecutionTime;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.exception.NotFoundException;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository repository;
    private final LinkInfoProperty property;

    @Override
    @LogExecutionTime
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
    @LogExecutionTime
    public LinkInfoResponse getByShortLink(String shortLink) {
        return repository.findByShortLinkAndActiveIsTrueAndEndTimeIsAfter(shortLink).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Не удалось найти активную сущность по короткой ссылке: " + shortLink));
    }

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @LogExecutionTime
    public void deleteByLinkId(UUID id) {
        repository.deleteLink(id);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse updateLinkInfo(LinkInfoUpdateRequest linkInfo) {
        LinkInfo linkToUpdate = repository.findById(linkInfo.getId())
                .orElseThrow(() -> new NotFoundException("Не удалось найти сущность для обновления"));
        if (linkInfo.getLink() != null) {
            linkToUpdate.setLink(linkInfo.getLink());
        }
        if (linkInfo.getEndTime() != null) {
            linkToUpdate.setEndTime(linkInfo.getEndTime());
        }
        if (linkInfo.getDescription() != null) {
            linkToUpdate.setDescription(linkInfo.getDescription());
        }
        if (linkInfo.getActive() != null) {
            linkToUpdate.setActive(linkInfo.getActive());
        }
        return toResponse(repository.save(linkToUpdate));

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
