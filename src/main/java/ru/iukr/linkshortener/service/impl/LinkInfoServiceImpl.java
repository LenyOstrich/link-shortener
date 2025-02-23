package ru.iukr.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.iukr.linkshortener.annotation.LogExecutionTime;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.exception.NotFoundException;
import ru.iukr.linkshortener.exception.NotFoundShortLinkException;
import ru.iukr.linkshortener.mapper.LinkInfoMapper;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoMapper linkInfoMapper;
    private final LinkInfoRepository repository;
    private final LinkInfoProperty property;

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest body) {
        return repository.findByFilter(
                        body.getLinkPart(),
                        body.getEndTimeFrom(),
                        body.getEndTimeTo(),
                        body.getDescriptionPart(),
                        body.getActive()
                ).stream()
                .map(linkInfoMapper::toResponse)
                .toList();
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(property.shortLinkLength());
        LinkInfo linkInfo = linkInfoMapper.fromCreateRequest(linkInfoRequest, shortLink);
        LinkInfo savedLinkInfo = repository.save(linkInfo);
        return linkInfoMapper.toResponse(savedLinkInfo);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo activeShortLink = repository.findActiveShortLink(shortLink, LocalDateTime.now())
                .orElseThrow(() -> new NotFoundShortLinkException("Не удалось найти активную сущность по короткой ссылке: " + shortLink));
        repository.incrementOpeningCountByShortLink(shortLink);
        return linkInfoMapper.toResponse(activeShortLink);
    }

    @Override
    @LogExecutionTime
    public void deleteByLinkId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse updateLinkInfo(LinkInfoUpdateRequest linkInfo) {
        LinkInfo linkToUpdate = repository.findById(UUID.fromString(linkInfo.getId()))
                .orElseThrow(() -> new NotFoundException("Не удалось найти сущность для обновления"));
        if (linkInfo.getLink() != null) {
            linkToUpdate.setLink(linkInfo.getLink());
        }
        if (linkInfo.getEndTime() != null) {
            linkToUpdate.setEndTime(linkInfo.getEndTime());
        } else {
            linkToUpdate.setEndTime(null);
        }
        if (linkInfo.getDescription() != null) {
            linkToUpdate.setDescription(linkInfo.getDescription());
        }
        if (linkInfo.getActive() != null) {
            linkToUpdate.setActive(linkInfo.getActive());
        }
        return linkInfoMapper.toResponse(repository.save(linkToUpdate));

    }
}
