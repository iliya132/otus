package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.models.ClientDto;
import ru.otus.services.ClientService;


@RestController
@RequestMapping("/api")
public class ApiController {
    private final ClientService clientService;

    @Autowired
    public ApiController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "/create-client")
    public String createClient(@RequestBody ClientDto clientDto) {
        clientService.save(clientDto);
        return "/index";
    }
}
