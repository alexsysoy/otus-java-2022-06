package ru.otus.services;

import ru.otus.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    Optional<Client> findById(long id);
    Client findByName(String name);
    Client findRandom();
    Client save(Client client);
}
