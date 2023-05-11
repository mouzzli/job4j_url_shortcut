package ru.jobj4.url_shortcut.dto;

import lombok.Data;
import ru.jobj4.url_shortcut.model.Url;

@Data
public class UrlDto {

    public UrlDto(Url url) {
        this.site = url.getUrl();
        this.total = url.getCounter();
    }

    private String site;
    private int total;
}
