package com.mindhub.homebanking.services;

import java.util.Set;

import com.mindhub.homebanking.dtos.PaymentDTO;

import org.springframework.http.ResponseEntity;

public interface PaymentService {
    
    ResponseEntity<Object> createPayment(PaymentDTO paymentDTO);

    Set<PaymentDTO> getPaymentsDtos(String cardNumber);
    
}
