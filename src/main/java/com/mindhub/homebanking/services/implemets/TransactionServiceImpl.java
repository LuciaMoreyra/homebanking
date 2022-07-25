package com.mindhub.homebanking.services.implemets;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @Override
    public Boolean saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction) != null;
    }

    @Override
    @Transactional
   public ResponseEntity<Object> createTransaction(double amount, String description, String numberOrigin, String numberDestination, Authentication authentication){
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

    @Override
    public Set<TransactionDTO> getTransactionDtos(String accountNumber, Optional<LocalDate> from, Optional<LocalDate> to) {
        Account account = accountRepository.findByNumber(accountNumber);
        Set<Transaction> transactions;
        if (account == null || !account.getIsActive()) {
            return new HashSet<>();
        }

        if (from.isEmpty() || to.isEmpty() ){
            transactions = transactionRepository.findByAccountId(account.getId());
        }else{
            transactions = transactionRepository.findByAccountIdAndDateBetween(account.getId(), from.get().atStartOfDay(), to.get().atTime(LocalTime.MAX));
        }
        return transactions.stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }


}

