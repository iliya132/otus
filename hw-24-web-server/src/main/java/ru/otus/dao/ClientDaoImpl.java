package ru.otus.dao;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.models.Client;

import java.util.List;

public class ClientDaoImpl implements ClientDao {
    private final TransactionManager transactionManager;
    private final DataTemplate<Client> clientTemplate;

    public ClientDaoImpl(TransactionManager transactionManager, DataTemplateHibernate<Client> clientTemplate) {
        this.transactionManager = transactionManager;
        this.clientTemplate = clientTemplate;
    }

    @Override
    public Client createClient(String name) {
        return transactionManager.doInTransaction(session -> clientTemplate.insert(session, new Client(name)));
    }

    @Override
    public List<Client> getClients() {
        return transactionManager.doInReadOnlyTransaction(clientTemplate::findAll);
    }
}
