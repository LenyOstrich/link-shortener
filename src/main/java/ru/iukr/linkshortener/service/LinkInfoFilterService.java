package ru.iukr.linkshortener.service;

import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfoResponse;

public interface LinkInfoFilterService {

    boolean applyFilters(LinkInfoResponse linkInfoResponse, FilterLinkInfoRequest body);
}
