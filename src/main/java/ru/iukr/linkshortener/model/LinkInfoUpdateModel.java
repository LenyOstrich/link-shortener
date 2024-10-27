package ru.iukr.linkshortener.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfoUpdateModel {
    private UUID id;
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
