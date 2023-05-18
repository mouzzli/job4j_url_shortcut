package ru.jobj4.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteDto {
    private boolean registration;
    private String login;
    private String password;
}
