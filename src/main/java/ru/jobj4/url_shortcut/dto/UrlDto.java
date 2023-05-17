package ru.jobj4.url_shortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.jobj4.url_shortcut.model.Url;

@Data
@AllArgsConstructor
public class UrlDto {

    public UrlDto(Url url) {
        this.site = url.getUrl();
        this.total = url.getCounter();
    }

    private String site;
    private int total;
}
