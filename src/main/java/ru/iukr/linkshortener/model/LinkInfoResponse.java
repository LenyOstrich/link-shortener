package ru.iukr.linkshortener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkInfoResponse{

    private UUID id;
    private String link;
    private String shortLink;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
    private Long openingCount;
}
