package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        // .map(client -> new ClientDTO(client))
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

     @RequestMapping("/clients/{id}")
     public ClientDTO getClient(@PathVariable long id) {
         return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
     }

    @GetMapping("/clients/current")
    @ResponseBody
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @NotNull @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client cliente = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account("VIN" + Integer.toString(getRandomNumber(0, 100000000)));
        cliente.addAccount(account);
        clientRepository.save(cliente);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
