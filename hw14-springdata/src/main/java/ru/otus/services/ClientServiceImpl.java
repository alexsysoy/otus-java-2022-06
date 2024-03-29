package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domain.Client;
import ru.otus.repostory.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clientRepository.findAll());
    }

    @Override
    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client findByName(String name) {
        return clientRepository.findByName(name).stream().findFirst().orElseThrow();
    }

    @Override
    public Client findRandom() {
        List<Client> clients = clientRepository.findAll();
        Random r = new Random();
        return clients.stream().skip(r.nextInt(clients.size() - 1)).findFirst().orElse(null);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
