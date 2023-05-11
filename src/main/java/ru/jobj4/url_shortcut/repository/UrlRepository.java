package ru.jobj4.url_shortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.jobj4.url_shortcut.model.Url;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Integer> {

    Optional<Url> findByCode(String code);


    @Modifying
    @Query("UPDATE Url u SET u.counter = u.counter + 1 WHERE u.id = :id")
    void incrementCount( int id);
}
