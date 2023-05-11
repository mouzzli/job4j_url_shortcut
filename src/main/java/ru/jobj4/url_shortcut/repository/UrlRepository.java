package ru.jobj4.url_shortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.url_shortcut.model.Url;

public interface UrlRepository extends CrudRepository<Url, Integer> {
}
