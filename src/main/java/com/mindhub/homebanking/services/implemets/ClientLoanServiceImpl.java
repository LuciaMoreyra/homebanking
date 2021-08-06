package com.mindhub.homebanking.services.implemets;

import java.util.HashSet;
import java.util.Set;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Transactional
    @Override
    public ResponseEntity<Object> create(Authentication authentication,LoanApplicationDTO loanApplicationDTO){
     // Verificar que los parámetros no estén vacíos
        if (loanApplicationDTO.getLoanId() <= 0 || loanApplicationDTO.getDestinationAccountNumber().isEmpty()
                || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

         Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        
        Client client = clientService.getClientAuth(authentication);

        // verificar que existan loan y client
        if (loan == null) {
            return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN);
        }
        if (client == null) {
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }

        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("amount is greater than max amount for this loan", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("payments are not available for this loan", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.getAccount(loanApplicationDTO.getDestinationAccountNumber());
        if (account == null) {
            return new ResponseEntity<>("account does not exist", HttpStatus.FORBIDDEN);
        }
        if (account.getClient() != client) {
            return new ResponseEntity<>("account does not belong to auth client", HttpStatus.FORBIDDEN);
        }

        createClientLoan(account, loan, loanApplicationDTO);
        return new ResponseEntity<>("created", HttpStatus.CREATED);
    
    }


    @Transactional
    @Override
    public ClientLoan createClientLoan(Account account, Loan loan , LoanApplicationDTO loanApplicationDTO){
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),
                " préstamo " + loan.getName() + " aprobado", (account.getBalance() + loanApplicationDTO.getAmount()));
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        double interests = loanApplicationDTO.getAmount() * loan.getPercentage() / 100;
        ClientLoan assignedLoan = new ClientLoan(account.getClient(), loan, loanApplicationDTO.getAmount() + interests,
                loanApplicationDTO.getPayments());
        return clientLoanRepository.save(assignedLoan);
    }

    @Override
    public Set<ClientLoan> getClientLoans(){
        return new HashSet<>(clientLoanRepository.findAll());
    }

    @Override
    public ClientLoan getClientLoan(Long id){
       return clientLoanRepository.getOne(id);
    }


}
