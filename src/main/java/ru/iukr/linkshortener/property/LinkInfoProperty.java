package ru.iukr.linkshortener.property;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("link-shortener")
public record LinkInfoProperty(
        @Min(value = 8, message = "Длина короткой ссылки не может быть меньше 8")
        Integer shortLinkLength
) {
}
