package com.mindhub.homebanking.services.implemets;

import java.util.Random;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService{
    @Autowired 
    CardRepository cardRepository;


    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }


    @Override
    public Card getCard(String number){
       return cardRepository.findByNumber(number);
    }


    @Override 
    public void deleteCardById(Long id){
        cardRepository.deleteById(id);
    }

    @Override
    public Boolean checkCvv(String number, int cvv){
     Card card = getCard(number);
     return (card.getCvv() == cvv);
    }

    @Override
    public Boolean createCard(CardType type, CardColor color,  Client client){
       String number = "";
        for (int i = 0; i < 4; i++) {
            number += Integer.toString(getRandomNumber(1000, 10000));
        }
        int cvv = getRandomNumber(100, 1000);
        Card card = new Card(type, color, client, number, cvv);
        return cardRepository.save(card)!= null ;

    }

    
}
