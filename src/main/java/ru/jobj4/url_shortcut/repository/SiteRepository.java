package ru.jobj4.url_shortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.url_shortcut.model.Site;

import java.util.Optional;

public interface SiteRepository  extends CrudRepository<Site, Integer> {

    Optional<Site> findByLogin(String login);
}
