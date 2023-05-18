package ru.jobj4.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlDto {

    private String site;

    private int total;
}
