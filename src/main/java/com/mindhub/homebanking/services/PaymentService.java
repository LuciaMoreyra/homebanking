package com.mindhub.homebanking.services;

import java.util.Set;

import com.mindhub.homebanking.dtos.PaymentApplicationDTO;

import com.mindhub.homebanking.dtos.PaymentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    
    ResponseEntity<Object> createPayment(PaymentApplicationDTO paymentDTO);

    ResponseEntity<Object>  getPaymentDtos(Authentication authentication, Long cardId);
    
}
