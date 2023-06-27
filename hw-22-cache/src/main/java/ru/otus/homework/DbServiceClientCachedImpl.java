package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCachedImpl implements DBServiceClient {
    private final DBServiceClient serviceClient;
    private final Logger logger = LoggerFactory.getLogger(DbServiceClientCachedImpl.class);
    MyCache<Long, Client> cache = new MyCache<>();

    public DbServiceClientCachedImpl(DBServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        cache.addListener(new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
    }

    @Override
    public Client saveClient(Client client) {
        return serviceClient.saveClient(client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cached = cache.get(id);
        if (cached != null) return Optional.of(cached);
        // .orElseThrow потому что не тема ДЗ - тут есть уверенность что такая сущность есть
        var fromDb = serviceClient.getClient(id).orElseThrow();
        cache.put(id, fromDb);
        return Optional.of(fromDb);
    }

    @Override
    public List<Client> findAll() {
        return serviceClient.findAll();
    }


}
