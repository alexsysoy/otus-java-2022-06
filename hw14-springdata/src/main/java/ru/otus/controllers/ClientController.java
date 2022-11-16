package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.Client;
import ru.otus.services.ClientService;
import ru.otus.utils.ClientUtils;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    private final ClientUtils clientUtils;

    public ClientController(ClientService clientService, ClientUtils clientUtils) {
        this.clientService = clientService;
        this.clientUtils = clientUtils;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client) {
        clientUtils.convertStringToPhonesWithoutDTO(client);
        clientService.save(client);
        return new RedirectView("/", true);
    }
}
