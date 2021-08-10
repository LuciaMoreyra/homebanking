package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> register( Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        return cardService.register(authentication, color, type);
    }

    @GetMapping("/clients/current/cards")
    @ResponseBody
    public Set<CardDTO> getClientCards(Authentication authentication) {
       return cardService.getCardsDTO(authentication);
    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam Long cardId) {
        return cardService.deleteCardById(cardId, authentication);
    }
}