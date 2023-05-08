package ru.jobj4.url_shortcut.service;

import ru.jobj4.url_shortcut.dto.SiteDto;
import ru.jobj4.url_shortcut.model.Site;

public interface SiteService {
    SiteDto registration(Site site);
}
