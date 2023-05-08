package ru.jobj4.url_shortcut.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

@Entity
@Table(name = "site")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @URL(message = "url must be valid")
    private String site;
    private String login;
    private String password;

}
