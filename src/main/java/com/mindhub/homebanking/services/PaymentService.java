package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.PaymentApplicationDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    
    ResponseEntity<Object> createPayment(PaymentApplicationDTO paymentDTO);

    ResponseEntity<Object>  getPaymentDtos(Authentication authentication, Long cardId);
    
}
