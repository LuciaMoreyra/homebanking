package com.mindhub.homebanking.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.services.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class PaymentController {
    
    @Autowired 
    PaymentService paymentService;

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<Object> create(@RequestBody PaymentDTO paymentDTO){
       return paymentService.createPayment(paymentDTO);
       
    }

    @GetMapping("/payments")
    public ResponseEntity<Object> getPayments(String cardNumber){
        if(cardNumber.isEmpty()){
        return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(paymentService.getPaymentsDtos(cardNumber), HttpStatus.OK);
    }

}
