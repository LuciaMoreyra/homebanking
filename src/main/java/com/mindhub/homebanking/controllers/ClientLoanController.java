package com.mindhub.homebanking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

//import org.jetbrains.annotations.NotNull;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;

    @Transactional
    @PostMapping("/client-loan")
    public ResponseEntity<Object> createLoan(Authentication authentication,
            @RequestBody LoanApplicationDTO loanApplicationDTO) {

        return clientLoanService.create(authentication, loanApplicationDTO);
    }

}