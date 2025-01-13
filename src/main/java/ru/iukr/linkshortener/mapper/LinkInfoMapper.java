package ru.iukr.linkshortener.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;

@Mapper(componentModel = "spring")
public interface LinkInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingCount", constant = "0L")
    LinkInfo fromCreateRequest(CreateLinkInfoRequest request, String shortLink);

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
