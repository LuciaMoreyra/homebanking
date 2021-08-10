package com.mindhub.homebanking.services;

import java.util.Set;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;


public interface LoanService {

    Boolean loanExists(String name);

    ResponseEntity<Object> createLoan(LoanDTO loanDTO);

    Set<LoanDTO> getLoans();

    LoanDTO getLoan(Long id);
    
}
