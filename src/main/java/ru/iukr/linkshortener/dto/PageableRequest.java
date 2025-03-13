package ru.iukr.linkshortener.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {

    @NotNull(message = "Не задан номер страницы")
    @Positive(message = "Номер страницы не может быть меньше одного")
    private Integer number;

    @NotNull(message = "Не задан размер страницы")
    @Positive(message = "Размер страницы не может быть меньше одного")
    private Integer size;

    @Valid
    @Builder.Default
    private List<SortRequest> sorts = new ArrayList<>();
}
