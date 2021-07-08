package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> register(
            @NotNull @RequestParam double amount, @RequestParam String description,
            @NotNull @RequestParam String numberOrigin, @RequestParam String numberDestination, Authentication authentication
    ){
        // Verificar que los parámetros no estén vacíos
        if (numberOrigin.isEmpty() || numberDestination.isEmpty() || amount == 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
       // Verificar que los números de cuenta no sean iguales
        if (numberDestination.equals(numberOrigin)){
            return new ResponseEntity<>("account numbers are equal", HttpStatus.FORBIDDEN);
        }

        Client client =  clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(numberOrigin);
        Account destinationAccount = accountRepository.findByNumber(numberDestination);

        // Verificar que exista la cuenta de origen
        if (originAccount == null) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }
        // Verificar que exista la cuenta de destino
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        // Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (!client.getAccounts().contains(originAccount)){
            return  new ResponseEntity<>("origin account does not belong to authenticated client", HttpStatus.FORBIDDEN);
        }
        // Verificar que la cuenta de origen tenga el monto disponible
        if (originAccount.getBalance() < amount) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, -amount, description + " " + destinationAccount.getNumber());
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, amount, description + " " + originAccount.getNumber());

        originAccount.addTransaction(transactionOrigin);
        destinationAccount.addTransaction(transactionDestination);

        transactionRepository.save(transactionOrigin);
        transactionRepository.save(transactionDestination);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
