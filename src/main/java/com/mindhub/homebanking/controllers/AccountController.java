package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.dtos.AccountDTO;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Set;



@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount( Authentication authentication, @RequestParam AccountType type){

       Client client = clientService.getClientAuth(authentication);
        return accountService.createNewAccount(client, type) ? new ResponseEntity<>(HttpStatus.CREATED): new ResponseEntity<>("Client already has 3 accounts", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/clients/current/accounts")
    @ResponseBody
    public Set<AccountDTO> getClientAccounts(Authentication authentication){
        return accountService.getClientAccountsDTO(authentication);
    }

    @GetMapping("/clients/current/account")
    @ResponseBody
    public AccountDTO getAccount(Authentication authentication,  @RequestParam String number){
        return accountService.getAccountDTO(authentication, number);
    }
    
    @PostMapping("/clients/current/account")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number){
        return accountService.deactivateAccount(authentication, number);
    }


}
