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
    }

}
