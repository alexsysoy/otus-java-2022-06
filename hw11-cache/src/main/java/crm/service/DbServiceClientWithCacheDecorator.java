package crm.service;

import cachehw.HwCache;
import cachehw.HwListener;
import cachehw.MyCache;
import crm.model.Client;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;


@Slf4j
public class DbServiceClientWithCacheDecorator implements DBServiceClient, HwListener<Long, Client> {
    private final DBServiceClient dbServiceClient;
    private final HwCache<Long, Client> cache;


    public DbServiceClientWithCacheDecorator(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
        cache = new MyCache<>();
        cache.addListener(this);
    }


    @Override
    public Client saveClient(Client client) {
        Client clientClone = client.clone();
        var result = dbServiceClient.saveClient(clientClone);
        cache.put(result.getId(),result.clone());
        return result.clone();
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cache.get(id);
        if (client == null) {
            log.info("There isn't client in cache. Going to db");
            var optionalClient = dbServiceClient.getClient(id);
            optionalClient.ifPresent(value -> cache.put(value.getId(), value.clone()));
            return optionalClient;
        }
        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        var clientList = dbServiceClient.findAll();
        clientList.forEach(client -> cache.put(client.getId(), client.clone()));
        return clientList;
    }

    @Override
    public void notify(Long key, Client value, String action) {
        log.info("key:{}, value:{}, action: {}", key, value, action);
    }
}
