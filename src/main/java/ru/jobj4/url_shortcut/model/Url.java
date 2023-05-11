package ru.jobj4.url_shortcut.model;

import jdk.jfr.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

@Entity
@Table(name = "url")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Name("long_url")
    @URL
    private String url;

    @Name("short_url")
    private String code;

    private int counter;
}
