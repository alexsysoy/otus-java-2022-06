package ru.otus.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
public class Client {

    @Id
    private Long id;
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Nickname nickname;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @PersistenceCreator
    public Client(Long id, String name, Address address, Nickname nickname, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.nickname = nickname;
        this.phones = phones;
    }

    public Client(String name, Address address, Nickname nickname, Set<Phone> phones) {
        this(null, name, address, nickname, phones);
    }

    public Client() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public void setNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", nickname=" + nickname +
                ", phones=" + phones +
                '}';
    }
}
