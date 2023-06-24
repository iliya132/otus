package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.models.Client;
import ru.otus.repository.IClientRepository;


@RestController
@RequestMapping("/api")
public class ApiController {
    private final IClientRepository clientRepository;

    @Autowired
    public ApiController(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping(value = "/create-client", consumes = "text/plain")
    public String createClient(@RequestBody String name) {
        clientRepository.save(new Client(name));
        return "/index";
    }
}
