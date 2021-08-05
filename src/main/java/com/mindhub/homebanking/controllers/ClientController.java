package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Client;


import com.mindhub.homebanking.services.ClientService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    @RequestMapping("/clients")
    public Set<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }

     @RequestMapping("/clients/{id}")
     public ClientDTO getClient(@PathVariable long id) {
         return clientService.getClientDTO(id);
     }*/

    @GetMapping("/clients/current")
    @ResponseBody
    public ClientDTO getClient(Authentication authentication){
        return  new ClientDTO(clientService.getClientAuth(authentication));
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @NotNull @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.clientExists(email)) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
