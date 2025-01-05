package ru.iukr.linkshortener.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.iukr.linkshortener.validation.ValidEndDate;
import ru.iukr.linkshortener.validation.ValidUUID;

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
    @ValidEndDate(message = "Дата окончания действия ссылки не верна")
    private String endTime;
    private String description;
    private Boolean active;
}
