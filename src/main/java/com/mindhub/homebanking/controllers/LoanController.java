package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;

// import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class LoanController {

    final static Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @PostMapping("/loans")
    public ResponseEntity<Object> createNewType(@RequestBody LoanDTO loanDTO) {

     return loanService.createLoan(loanDTO);
    }
}
