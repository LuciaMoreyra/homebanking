package com.mindhub.homebanking.services.implemets;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.mindhub.homebanking.models.AccountType;

import java.util.Random;
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

    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findFirst().getAsInt();
    }

    @Override
    public Boolean accountBelongsToAuthClient(Authentication authentication, Account account) {
        Client client = clientService.getClientAuth(authentication);
        // return (!(client.getAccounts().contains(account)));
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
            int number = getRandomNumber(0, 100000000);
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
        // return client.getActiveAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        return accountRepository.findByClientId(client.getId()).stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Boolean deactivateAccount(Account account) {
        try {
            account.setIsActive(false);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
