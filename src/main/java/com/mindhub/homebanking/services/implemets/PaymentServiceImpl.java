package com.mindhub.homebanking.services.implemets;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.PaymentApplicationDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.PaymentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    @Autowired
    ClientRepository clientRepository;

    @Override
    public ResponseEntity<Object> createPayment(PaymentApplicationDTO dto){
        if (dto.getCardNumber().isEmpty() || dto.getAmount() <= 0 || dto.getDescription().isEmpty() || dto.getCvv() < 100 ||  dto.getCvv() >= 1000 ){
            return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }
        Card card = cardRepository.findByNumber(dto.getCardNumber());
        if (card == null){
            return new ResponseEntity<>("card does not exist", HttpStatus.FORBIDDEN);
    }
        // if (!cardService.checkCvv(dto.getCardNumber(), dto.getCvv())){
        if (card.getCvv() != dto.getCvv()){
            return new ResponseEntity<>("card number and cvv do not match", HttpStatus.FORBIDDEN); 
        }
     
        if (card.getThruDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("card is expired", HttpStatus.FORBIDDEN);
        }
        Set<Account> accounts = accountRepository.findByClientIdAndBalanceGreaterThan(card.getClient().getId(), dto.getAmount());

        // for (Account account : accounts) {
        //     logger.info(account.getNumber() + " ++++++ " + account.getBalance());
        // }
        if (accounts.size() == 0){
            return new ResponseEntity<>("you do not have enough money in any of your accounts", HttpStatus.FORBIDDEN);
        }
        // toma el primer elemento del set
        Account account= accounts.stream().findFirst().get();
        Transaction transaction = new Transaction(TransactionType.DEBIT, -dto.getAmount(), dto.getDescription(), account.getBalance() - dto.getAmount());
        account.addTransaction(transaction);
        
        transactionRepository.save(transaction);
        
        Payment payment = new Payment(dto.getAmount(),dto.getDescription(),card, transaction.getDate(), account.getNumber());
        paymentRepository.save(payment);
        logger.info("--------------- ---------- -----------------");
         for (Payment p : card.getPayments()) {
             logger.info(p.getId().toString() + " ------ " + p.getDescription() + "--------"  + p.getAmount());
         }
        logger.info("--------------- ---------- -----------------");
        return new ResponseEntity<>("payment completed",HttpStatus.CREATED);   
    }

   @Override
    public ResponseEntity<Object>  getPaymentDtos(Authentication authentication, Long cardId){
       if(cardId <= 0 || cardId == null){
           return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
       }
       // verificar que la tarjeta exista
       if (!cardRepository.existsCardById(cardId)){
           return new ResponseEntity<>("card does not exist", HttpStatus.FORBIDDEN);
       }
       Card card = cardRepository.getOne(cardId);

       // verificar que la tarjeta pertenezca al cliente autentificado
       Client client = clientRepository.findByEmail(authentication.getName());
       if(card.getClient() != client){
           return new ResponseEntity<>("card does not belong to auth client", HttpStatus.FORBIDDEN);
       }
       // verificar que la tarjeta no este vencida
       if (card.getThruDate().isBefore(LocalDateTime.now())){
           return new ResponseEntity<>("card is expired", HttpStatus.FORBIDDEN);
       }

       Set<PaymentDTO> payments = paymentRepository.findByCardId(cardId).stream().map(PaymentDTO::new).collect(Collectors.toSet());
       return new ResponseEntity<>(payments, HttpStatus.OK);
   }

}