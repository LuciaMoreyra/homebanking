package com.mindhub.homebanking.services.implemets;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Payment;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.PaymentRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.PaymentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PaymentServiceImpl implements PaymentService{
    final static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    

    @Override
    public ResponseEntity<Object> createPayment(PaymentDTO paymentDTO){
        if (paymentDTO.getCardNumber().isEmpty() || paymentDTO.getAmount() <= 0 || paymentDTO.getDescription().isEmpty() || paymentDTO.getCvv() < 100 ||  paymentDTO.getCvv() >= 1000 ){
            return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }
        Card card = cardRepository.findByNumber(paymentDTO.getCardNumber());
        if (card == null){
            return new ResponseEntity<>("card does not exist", HttpStatus.FORBIDDEN);
    }
        // if (!cardService.checkCvv(paymentDTO.getCardNumber(), paymentDTO.getCvv())){
        if (card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("card number and cvv do not match", HttpStatus.FORBIDDEN); 
        }
     
        if (card.getThruDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("card is expired", HttpStatus.FORBIDDEN);
        }
        Set<Account> accounts = accountRepository.findByClientIdAndBalanceGreaterThan(card.getClient().getId(), paymentDTO.getAmount());
        // for (Account account : accounts) {
        //     logger.info(account.getNumber() + " ++++++ " + account.getBalance());
        // }
        if (accounts.size() == 0){
            return new ResponseEntity<>("you do not have enough money in any of your accounts", HttpStatus.FORBIDDEN);
        }
        // toma el primer elemento del set
        Account account= accounts.stream().findFirst().get();
        Transaction transaction = new Transaction(TransactionType.DEBIT, -paymentDTO.getAmount(), paymentDTO.getDescription(), account.getBalance() - paymentDTO.getAmount());
        account.addTransaction(transaction);
        
        transactionRepository.save(transaction);
        
        Payment payment = new Payment(paymentDTO.getCardNumber(),paymentDTO.getCvv(),paymentDTO.getAmount(),paymentDTO.getDescription(),card);
        paymentRepository.save(payment);
        logger.info("--------------- ---------- -----------------");
         for (Payment p : card.getPayments()) {
             logger.info(p.getId().toString() + " ------ " + p.getDescription() + "--------"  + p.getAmount());
         }
        logger.info("--------------- ---------- -----------------");
        
        
         
        return new ResponseEntity<>("payment completed",HttpStatus.CREATED);   
    }

    @Override
    public Set<PaymentDTO> getPaymentsDtos(String cardNumber){
    return paymentRepository.findByCardNumber(cardNumber).stream().map(PaymentDTO::new).collect(Collectors.toSet());
    }

}