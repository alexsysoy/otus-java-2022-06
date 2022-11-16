package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {

    @Id
    private final Long clientId;

    private final String street;

    @PersistenceCreator
    public Address(Long clientId, String street) {
        this.clientId = clientId;
        this.street = street;
    }

    public Address(String street) {
        this(null, street);
    }

    public Long getClientId() {
        return clientId;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return street;
    }
}
