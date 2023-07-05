package ru.otus.services;

import java.util.Arrays;
import java.util.List;

import ru.otus.models.Address;
import ru.otus.models.Client;
import ru.otus.models.ClientDto;
import ru.otus.models.Phone;
import ru.otus.repository.IClientRepository;

public class ClientService {
    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void save(ClientDto client) {
        var phones = Arrays
                .stream(client.getPhone().split(";"))
                .map(it -> new Phone(null, it))
                .toList();

        var address = new Address(null, client.getAddress());

        Client newClient = new Client(
                null,
                client.getName(),
                address,
                phones
        );
        clientRepository.save(newClient);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
