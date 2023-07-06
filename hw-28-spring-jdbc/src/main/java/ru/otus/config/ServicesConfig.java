package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.repository.IClientRepository;
import ru.otus.services.ClientService;

@Configuration
public class ServicesConfig {
    private final IClientRepository clientRepository;

    public ServicesConfig(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Bean
    public ClientService clientService() {
        return new ClientService(clientRepository);
    }
}
