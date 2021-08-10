package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {
    Card getCard(String number);

    Set<CardDTO> getCardsDTO(Authentication authentication);

    Boolean checkCvv(String number, int cvv);

    Boolean createCard(CardType type, CardColor color,  Client client);

    ResponseEntity<Object> register(Authentication authentication, CardColor color, CardType type);

    ResponseEntity<Object> deleteCardById(Long id, Authentication authentication);


}
