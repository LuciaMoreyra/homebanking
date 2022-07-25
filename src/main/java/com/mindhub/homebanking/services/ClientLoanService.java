package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


import java.util.Set;

public interface ClientLoanService {
    // get all the clientloans
    Set<ClientLoan> getClientLoans();
    // get one client loan
    ClientLoan getClientLoan(Long id);

    ClientLoan createClientLoan(Account account, Loan loan , LoanApplicationDTO loanApplicationDTO);
    
    ResponseEntity<Object> create(Authentication authentication,LoanApplicationDTO loanApplicationDTO);

}
