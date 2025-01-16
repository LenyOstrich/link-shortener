package ru.iukr.linkshortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.iukr.linkshortener.validation.ValidUUID;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfoUpdateRequest {

    @ValidUUID
    private String id;
    @Pattern(regexp = "^http[s]?://.+\\..+$", message = "url не соответствует паттерну")
    private String link;
    @Future(message = "Нельзя проставить дату окончания действия ссылки в прошлом")
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
