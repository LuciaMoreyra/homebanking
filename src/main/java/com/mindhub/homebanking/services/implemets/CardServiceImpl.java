package com.mindhub.homebanking.services.implemets;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;

import com.mindhub.homebanking.services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService{
    @Autowired 
    CardRepository cardRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;

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
    public ResponseEntity<Object> deleteCardById(Long id, Authentication authentication){

        if (id <= 0 || id == null) {
            return new ResponseEntity<>("card does not exist", HttpStatus.NOT_FOUND);
        }
        Client clientAuth = clientService.getClientAuth(authentication);
        if (clientAuth.getCards()
                .stream()
                .filter(card -> card.getId().equals(id))
                .count() == 0){
            return new ResponseEntity<>("card does not belong to auth client", HttpStatus.NOT_FOUND);
        }
        cardRepository.deleteById(id);
        
        return new ResponseEntity<>(HttpStatus.OK);
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

    @Override
    public ResponseEntity<Object> register(Authentication authentication, CardColor color, CardType type){
        if (type.toString().isEmpty() ||  color.toString().isEmpty()){
            return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.getClientAuth(authentication);
        if (client.getCards()
                .stream()
                .filter(card -> card.getType() == type)
                .collect(Collectors.toSet())
                .size() >= 3) {
            return new ResponseEntity<>("already has 3 cards this type", HttpStatus.FORBIDDEN);
        }
        createCard(type, color, client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Override
   public Set<CardDTO> getCardsDTO(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(Collectors.toSet());
    }
    
}
