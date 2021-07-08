package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> register(@NotNull Authentication authentication, @RequestParam  CardType type,

             @RequestParam CardColor color){

        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toSet()).size() >= 3){
            return new ResponseEntity<>("already has 3 cards this type", HttpStatus.FORBIDDEN);
        }
        Card card = new Card(type, color, client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    @ResponseBody
    public Set<CardDTO> getClientCards(@NotNull Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

}
