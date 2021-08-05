package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.dtos.AccountDTO;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.jetbrains.annotations.NotNull;
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
    public ResponseEntity<Object> createAccount(@NotNull Authentication authentication, @RequestParam AccountType type){

       Client client = clientService.getClientAuth(authentication);
        return accountService.createNewAccount(client, type) ? new ResponseEntity<>(HttpStatus.CREATED): new ResponseEntity<>("Client already has 3 accounts", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/clients/current/accounts")
    @ResponseBody
    public Set<AccountDTO> getClientAccounts(@NotNull Authentication authentication){
        return accountService.getClientAccountsDTO(authentication);
    }

    @GetMapping("/clients/current/account")
    @ResponseBody
    public AccountDTO getAccount(@NotNull Authentication authentication,  @RequestParam String number){
    //         Account account = accountService.getAccount(number);
    //         if (account == null || !account.getIsActive()) {
    //             return null;
    //         }
    //         Boolean b = accountService.accountBelongsToAuthClient(authentication, account);
    //         if (Boolean.TRUE.equals(b)) {
    //             return  new AccountDTO(account);
    //         } 
    //         return null;
        return accountService.getAccountDTO(authentication, number);
    }
    
    @PostMapping("/clients/current/account")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number){
        Account account = accountService.getAccount(number);
        if (account == null) {
            return new ResponseEntity<>("account does not exist", HttpStatus.NOT_FOUND);
        }
        
        Client client = clientService.getClientAuth(authentication);
        if (client != account.getClient()){
            return new ResponseEntity<>("account does not belong to this client", HttpStatus.FORBIDDEN);
        }
        
        if (account.getBalance() > 0 ){
            return new ResponseEntity<>("account must be empty when delete", HttpStatus.FORBIDDEN);
        }

        int accountsLength = client.getActiveAccounts().size();
        if (accountsLength == 1){
            return new ResponseEntity<>("client must have at least one account", HttpStatus.FORBIDDEN);
        }

        if (accountService.deactivateAccount(account)){
            return new ResponseEntity<>("account deleted", HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


}
