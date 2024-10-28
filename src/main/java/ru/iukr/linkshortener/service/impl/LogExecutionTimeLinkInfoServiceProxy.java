package ru.iukr.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.dto.UpdateRequest;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class LogExecutionTimeLinkInfoServiceProxy implements LinkInfoService {

    private final LinkInfoService linkInfoService;

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest linkInfoRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.createLinkInfo(linkInfoRequest);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения createLinkInfo: {}", stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.getByShortLink(shortLink);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения getByShortLink: {}", stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.findByFilter();
        } finally {
            stopWatch.stop();
            log.info("Время выполнения findByFilter: {}", stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public void deleteByLinkId(UUID id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            linkInfoService.deleteByLinkId(id);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения deleteByLinkId: {}", stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public LinkInfoResponse updateLinkInfo(UpdateRequest linkInfo) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
           return linkInfoService.updateLinkInfo(linkInfo);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения updateLinkInfo: {}", stopWatch.getTotalTimeMillis());
        }
    }
}
