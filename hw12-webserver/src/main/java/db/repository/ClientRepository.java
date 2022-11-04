package db.repository;

import db.crm.model.Client;

import java.util.List;

public interface ClientRepository {
    Client saveClientInDb(Client client);
    List<Client> getAllClientFromDb();
}
