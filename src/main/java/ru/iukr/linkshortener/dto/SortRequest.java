package ru.iukr.linkshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    @NotEmpty(message = "Не указано поле для сортировки")
    String field;

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "Указано некорректное направление сортировки")
    String direction = "ASC";
}
