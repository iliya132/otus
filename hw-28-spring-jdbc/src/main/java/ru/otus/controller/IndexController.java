package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.repository.IClientRepository;

@Controller
public class IndexController {
    private final IClientRepository clientRepository;

    @Autowired
    public IndexController(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping(path = {"/index", "/"})

    public String index(Model model) {
        var clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "index";
    }
}
