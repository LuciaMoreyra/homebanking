package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

public interface CardService {
    Card getCard(String number);
    
    Boolean checkCvv(String number, int cvv);

    Boolean createCard(CardType type, CardColor color,  Client client);

    void deleteCardById(Long id);
}
