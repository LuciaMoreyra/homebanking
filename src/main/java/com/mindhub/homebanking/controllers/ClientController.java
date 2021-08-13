package com.mindhub.homebanking.controllers;

import java.util.Set;

import com.mindhub.homebanking.dtos.ClientDTO;


import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

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

    @GetMapping("/clients/current")
    @ResponseBody
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientService.getClientAuth(authentication));
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        return clientService.saveClient(firstName, lastName, email, password);
    }

    @GetMapping("/clients")
    public ResponseEntity<Object> getClients(Authentication authentication) {
        return clientService.getClientsDTO(authentication);
    }
}
