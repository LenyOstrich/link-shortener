package ru.iukr.linkshortener.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkInfoRequest {

    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;

}
