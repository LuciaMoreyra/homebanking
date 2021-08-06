package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;

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
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> register( Authentication authentication, @RequestParam CardType type,

            @RequestParam CardColor color) {

        Client client = clientService.getClientAuth(authentication);

        if (client.getCards()
        .stream()
        .filter(card -> card.getType() == type)
        .collect(Collectors.toSet())
        .size() >= 3) {
            return new ResponseEntity<>("already has 3 cards this type", HttpStatus.FORBIDDEN);
        }
        cardService.createCard(type, color, client);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/clients/current/cards")
    @ResponseBody
    public Set<CardDTO> getClientCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards()
        .stream()
        .map(card -> new CardDTO(card))
        .collect(Collectors.toSet());
    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam Long cardId) {
        if (cardId <= 0 || cardId == null) {
            return new ResponseEntity<>("card does not exist", HttpStatus.NOT_FOUND);
        }
        Client clientAuth = clientService.getClientAuth(authentication);
        if (clientAuth.getCards()
        .stream()
        .filter(card -> card.getId().equals(cardId))
        .count() == 0){
            return new ResponseEntity<>("card does not belong to auth client", HttpStatus.NOT_FOUND);
        }
        cardRepository.deleteById(cardId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}