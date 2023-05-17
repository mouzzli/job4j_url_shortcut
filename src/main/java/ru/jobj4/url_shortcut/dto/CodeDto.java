package ru.jobj4.url_shortcut.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jobj4.url_shortcut.model.Url;

@Data
@NoArgsConstructor
public class CodeDto {

    public CodeDto(Url url) {
        this.code = url.getCode();
    }

    private String code;
}
