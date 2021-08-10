package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface TransactionService {
    
    Boolean saveTransaction(Transaction transaction);

    ResponseEntity<Object> createTransaction(double amount, String description, String numberOrigin, String numberDestination, Authentication authentication);

    Set<TransactionDTO> getTransactionDtos(String accountNumber, Optional<LocalDate> from, Optional<LocalDate> to);
}
