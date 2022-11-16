package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("nickname")
public class Nickname {

    @Id
    private final Long id;

    private final String nick;

    @PersistenceCreator
    public Nickname(Long id, String nick) {
        this.id = id;
        this.nick = nick;
    }

    public Nickname(String nick) {
        this(null, nick);
    }

    public Long getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public String toString() {
        return nick;
    }
}
