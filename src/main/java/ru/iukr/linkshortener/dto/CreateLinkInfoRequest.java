package ru.iukr.linkshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.iukr.linkshortener.validation.ValidEndDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkInfoRequest {

    @NotEmpty(message = "Ссылка не может быть пустой")
    @Pattern(regexp = "^http[s]?://.+\\..+$", message = "url не соответствует паттерну")
    private String link;
    @ValidEndDate(message = "Дата окончания действия ссылки не верна")
    private String endTime;
    private String description;
    @NotNull(message = "Признак активности не может быть null")
    private Boolean active;

}
