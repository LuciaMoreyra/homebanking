package com.mindhub.homebanking.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mindhub.homebanking.dtos.PaymentApplicationDTO;
import com.mindhub.homebanking.services.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class PaymentController {
    
    @Autowired 
    PaymentService paymentService;

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<Object> create(@RequestBody PaymentApplicationDTO dto){
       return paymentService.createPayment(dto);
       
    }

    @GetMapping("/payments")
    public ResponseEntity<Object> getPayments(Authentication authentication, @RequestParam Long cardId){
        return paymentService.getPaymentDtos(authentication, cardId);
       /* if(cardId <= 0 || cardId == null){
        return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }
        // verificar que la tarjeta exista
        if (!cardRepository.existsCardById(cardId)){
            return new ResponseEntity<>("card does not exist", HttpStatus.FORBIDDEN);
        }
        Card card = cardRepository.getOne(cardId);
        // verificar que la tarjeta pertenezca al cliente autentificado
        Client client = clientRepository.findByEmail(authentication.getName());
        if(card.getClient() != client){
            return new ResponseEntity<>("card does not belong to auth client", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(paymentService.getPaymentDtos(cardId), HttpStatus.OK);*/
    }

}
