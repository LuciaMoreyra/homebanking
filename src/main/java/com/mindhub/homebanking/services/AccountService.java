package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface AccountService {

    Boolean createNewAccount(Client client, AccountType type);

    Set<AccountDTO> getClientAccountsDTO(Authentication authentication);

    AccountDTO getAccountDTO(Authentication authentication, String number);

    Account getAccount(String number);

    Boolean deactivateAccount(Account account);

    Boolean accountBelongsToAuthClient(Authentication authentication, Account account);
}
