package ru.iukr.linkshortener.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("link-shortener")
public class LinkInfoProperty {
   public Integer shortLinkLength;
}
