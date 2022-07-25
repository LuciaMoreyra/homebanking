package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.LoanService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @PostMapping("/loans")
    public ResponseEntity<Object> createNewType(@RequestBody LoanDTO loanDTO) {

     return loanService.createLoan(loanDTO);
    }
}
