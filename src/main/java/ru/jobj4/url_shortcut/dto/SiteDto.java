package ru.jobj4.url_shortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteDto {
    private boolean registration;
    private String login;
    private String password;
}
