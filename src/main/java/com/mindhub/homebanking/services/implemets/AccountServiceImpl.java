package com.mindhub.homebanking.services.implemets;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;

import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.mindhub.homebanking.models.AccountType;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;

    @Override
    public Boolean accountBelongsToAuthClient(Authentication authentication, Account account) {
        Client client = clientService.getClientAuth(authentication);
        return (client == account.getClient());
    }

    @Override
    public Account getAccount(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public AccountDTO getAccountDTO(Authentication authentication, String number){
            Account account = accountRepository.findByNumber(number);
            if (account == null || !account.getIsActive()) {
                return null;
            }
            Boolean b = accountBelongsToAuthClient(authentication, account);
            if (Boolean.TRUE.equals(b)) {
                return  new AccountDTO(account);
            } 
            return null;
    }

    @Override
    public Boolean createNewAccount(Client client, AccountType type) {
        if (client.getActiveAccounts().size() >= 3) {
            return false;
        }
        try {
            int number = CardUtils.getRandomNumber(0, 100000000);
            Account newAccount = new Account("VIN" + Integer.toString(number), type);
            client.addAccount(newAccount);
            accountRepository.save(newAccount);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Set<AccountDTO> getClientAccountsDTO(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return accountRepository.findByClientIdAndIsActiveTrue(client.getId())
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<Object> deactivateAccount(Authentication authentication, String number){
        Account account = accountRepository.findByNumber(number);
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
        try {
            account.setIsActive(false);
            accountRepository.save(account);
            return new ResponseEntity<>("account deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
