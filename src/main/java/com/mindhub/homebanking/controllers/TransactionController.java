package com.mindhub.homebanking.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;

// import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountService accountService;

    @GetMapping("/transactions")
    public Set<TransactionDTO> getTransactions(@RequestParam String number, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> from, @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> to){
        Account account = accountService.getAccount(number);
        Set<Transaction> transactions =  new HashSet<>();
        if (account == null || !account.getIsActive()) {
            return new HashSet<>();
        }

        if (from.isEmpty() || to.isEmpty() ){
            // return account.getTransactions()
            // .stream()
            // .map(TransactionDTO::new)
            // .collect(Collectors.toSet());
            transactions = transactionRepository.findByAccountId(account.getId());
        }else{
            transactions = transactionRepository.findByAccountIdAndDateBetween(account.getId(), from.get().atStartOfDay(), to.get().atTime(LocalTime.MAX));
        }
        return transactions.stream().map(TransactionDTO::new).collect(Collectors.toSet());
        // return account.getTransactions()
        // .stream()
        // // arreglarr jnsjsndjncodjnds
        // .filter(tr -> tr.getDate().toLocalDate().isBefore(to.get().plusDays(1)) && tr.getDate().toLocalDate().isAfter(from.get().minusDays(1)))
        // .map(TransactionDTO::new)
        // .collect(Collectors.toSet());
    }

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> register(
             @RequestParam double amount, @RequestParam String description,
             @RequestParam String numberOrigin, @RequestParam String numberDestination, Authentication authentication
    ){
        // Verificar que los parámetros no estén vacíos
        if (numberOrigin.isEmpty() || numberDestination.isEmpty() || amount <= 0) {
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
            return new ResponseEntity<>("account balance is not enough", HttpStatus.FORBIDDEN);
        }

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, -amount, description + " " + destinationAccount.getNumber(), (originAccount.getBalance() - amount));
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, amount, description + " " + originAccount.getNumber(), (destinationAccount.getBalance() + amount));

        originAccount.addTransaction(transactionOrigin);
        destinationAccount.addTransaction(transactionDestination);

        transactionRepository.save(transactionOrigin);
        transactionRepository.save(transactionDestination);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
