package ru.iukr.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.service.LinkInfoFilterService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LinkInfoFilterServiceImpl implements LinkInfoFilterService {

    @Override
    public boolean applyFilters(LinkInfoResponse linkInfoResponse, FilterLinkInfoRequest body) {
        return isLinkPartValid(linkInfoResponse, body.getLinkPart()) &&
                isEndTimeFromValid(linkInfoResponse, body.getEndTimeFrom()) &&
                isEndTimeToValid(linkInfoResponse, body.getEndTimeTo()) &&
                isDescriptionPartValid(linkInfoResponse, body.getDescriptionPart()) &&
                isActiveValid(linkInfoResponse, body.getActive());
    }

    private boolean isActiveValid(LinkInfoResponse linkInfoResponse, Boolean active) {
        if (active == null) {
            return true;
        }
        return linkInfoResponse.getActive().equals(active);
    }

    private boolean isDescriptionPartValid(LinkInfoResponse linkInfoResponse, String descriptionPart) {
        if (descriptionPart == null) {
            return true;
        }
        return linkInfoResponse.getDescription().contains(descriptionPart);
    }

    private boolean isEndTimeToValid(LinkInfoResponse linkInfoResponse, LocalDateTime endTimeTo) {
        if (endTimeTo == null) {
            return true;
        }
        return linkInfoResponse.getEndTime().isBefore(endTimeTo);
    }

    private boolean isEndTimeFromValid(LinkInfoResponse linkInfoResponse, LocalDateTime endTimeFrom) {
        if (endTimeFrom == null) {
            return true;
        }
        return linkInfoResponse.getEndTime().isAfter(endTimeFrom);
    }

    private boolean isLinkPartValid(LinkInfoResponse linkInfoResponse, String linkPart) {
        if (linkPart == null) {
            return true;
        }
        return linkInfoResponse.getLink().contains(linkPart);
    }
}
