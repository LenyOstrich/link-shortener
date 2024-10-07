package ru.iukr.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateLinkInfoRequest {

    private String link;
    private LocalDateTime endTime;
    private String description;
    private boolean active;

}
