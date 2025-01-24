package ru.iukr.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterLinkInfoRequest {

    private String linkPart;
    private LocalDateTime endTimeTo;
    private LocalDateTime endTimeFrom;
    private String descriptionPart;
    private Boolean active;
}
