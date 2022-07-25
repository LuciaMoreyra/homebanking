package com.mindhub.homebanking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.services.ClientLoanService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    ClientLoanService clientLoanService;

    @Transactional
    @PostMapping("/client-loan")
    public ResponseEntity<Object> createLoan(Authentication authentication,
            @RequestBody LoanApplicationDTO loanApplicationDTO) {

        return clientLoanService.create(authentication, loanApplicationDTO);
    }

}