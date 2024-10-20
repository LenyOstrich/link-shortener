package ru.iukr.linkshortener.service.impl;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.exception.NotFoundException;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.List;

import static ru.iukr.linkshortener.constants.ShortLinkValues.LINK_LENGTH;

@NoArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private LinkInfoRepository repository;

    public LinkInfoServiceImpl(LinkInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(LINK_LENGTH);
        LinkInfo linkInfo = LinkInfo.builder()
                .shortLink(shortLink)
                .link(linkInfoRequest.getLink())
                .endTime(linkInfoRequest.getEndTime())
                .description(linkInfoRequest.getDescription())
                .active(linkInfoRequest.getActive())
                .openingCount(0L)
                .build();
        LinkInfo savedLinkInfo = repository.save(linkInfo);
        return toResponse(linkInfo);
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        return repository.findByShortLink(shortLink).map(this::toResponse)
                .orElseThrow(()-> new NotFoundException("Не удалось найти сущность по короткой ссылке: " + shortLink));
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

    @Override
    public List<LinkInfoResponse> findByFilter() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }
}
