package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Account;

// import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;


import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;



@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

/*    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

     @RequestMapping("/accounts/{id}")
     public AccountDTO getAccount(@PathVariable Long id){
         return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
     }*/


                
    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
          .findFirst()
          .getAsInt();
      }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> register(@NotNull Authentication authentication){
       Client client = clientRepository.findByEmail(authentication.getName());
        
       if(client.getAccounts().size() >= 3){        
        return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
       } 
       int number = getRandomNumber(0, 100000000);
        Account newAccount = new Account("VIN" + Integer.toString(number));
		client.addAccount(newAccount);
		accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    @ResponseBody
    public Set<AccountDTO> getClientAccounts(@NotNull Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @GetMapping("/clients/current/account")
    @ResponseBody
    public AccountDTO getClient(@NotNull Authentication authentication,  @RequestParam Long id){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        if (!(client.getAccounts().contains(account)) || (account == null)){
            return null;
        }
        return new AccountDTO(account);
      // return client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }
    
}
