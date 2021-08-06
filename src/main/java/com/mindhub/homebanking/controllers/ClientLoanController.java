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
    // // Verificar que los parámetros no estén vacíos
    // if (loanApplicationDTO.getLoanId() <= 0 ||
    // loanApplicationDTO.getDestinationAccountNumber().isEmpty()
    // || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getAmount() <=
    // 0) {
    // return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
    // }

    // // Loan loan =
    // // loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
    // Loan loan = loanService.getLoan(loanApplicationDTO.getLoanId());
    // Client client = clientService.getClientAuth(authentication);

    // // verificar que existan loan y client
    // if (loan == null) {
    // return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN);
    // }
    // if (client == null) {
    // return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
    // }

    // if (loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
    // return new ResponseEntity<>("amount is greater than max amount for this
    // loan", HttpStatus.FORBIDDEN);
    // }
    // if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
    // return new ResponseEntity<>("payments are not available for this loan",
    // HttpStatus.FORBIDDEN);
    // }
    // Account account =
    // accountService.getAccount(loanApplicationDTO.getDestinationAccountNumber());
    // if (account == null) {
    // return new ResponseEntity<>("account does not exist", HttpStatus.FORBIDDEN);
    // }
    // if (account.getClient() != client) {
    // return new ResponseEntity<>("account does not belong to auth client",
    // HttpStatus.FORBIDDEN);
    // }

    // /*
    // * Transaction transaction = new Transaction(TransactionType.CREDIT,
    // * loanApplicationDTO.getAmount(), " préstamo " + loan.getName() + "
    // aprobado",
    // * (account.getBalance() + loanApplicationDTO.getAmount()));
    // * account.addTransaction(transaction);
    // transactionRepository.save(transaction);
    // *
    // * double intrests = loanApplicationDTO.getAmount() * loan.getPercentage() /
    // * 100; ClientLoan prestamoAsignado = new ClientLoan(client, loan,
    // * loanApplicationDTO.getAmount() + intrests,
    // loanApplicationDTO.getPayments());
    // * clientLoanRepository.save(prestamoAsignado);
    // */
    // clientLoanService.createClientLoan(account, loan, loanApplicationDTO);
    // return new ResponseEntity<>("created", HttpStatus.CREATED);

}