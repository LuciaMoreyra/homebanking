package com.mindhub.homebanking.controllers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import com.mindhub.homebanking.dtos.TransactionDTO;

import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @GetMapping("/transactions")
    public Set<TransactionDTO> getTransactions(@RequestParam String number, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> from, @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> to){
        return transactionService.getTransactionDtos(number, from, to);
    }

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> register(
             @RequestParam double amount, @RequestParam String description,
             @RequestParam String numberOrigin, @RequestParam String numberDestination, Authentication authentication
    ){
        return transactionService.createTransaction(amount, description, numberOrigin, numberDestination, authentication);
    }

}
