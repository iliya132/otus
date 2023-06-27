package ru.otus.dao;

import ru.otus.models.Client;

import java.util.List;

public interface ClientDao {
    Client createClient(String name);
    List<Client> getClients();
}
