package ru.jobj4.urlshortcut.service;

import ru.jobj4.urlshortcut.dto.SiteDto;
import ru.jobj4.urlshortcut.model.Site;

public interface SiteService {
    SiteDto registration(Site site);

}
