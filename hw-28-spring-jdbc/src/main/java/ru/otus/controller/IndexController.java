package ru.otus.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.models.Client;
import ru.otus.models.ClientDto;
import ru.otus.models.Phone;
import ru.otus.services.ClientService;

@Controller
public class IndexController {
    private final ClientService clientService;

    @Autowired
    public IndexController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = {"/index", "/"})

    public String index(Model model) {
        var clients = clientService.findAll();
        model.addAttribute("clients", mapToDto(clients));
        return "index";
    }

    private List<ClientDto> mapToDto(List<Client> clients) {
        return clients.stream().map(it ->
                new ClientDto(it.getId(),
                        it.getName(),
                        it.getAddress().getStreet(),
                        it.getPhones().stream().map(Phone::getNumber)
                                .collect(Collectors.joining(";"))
                )).toList();
    }
}
