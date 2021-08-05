package com.mindhub.homebanking.services.implemets;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountService accountService;

    @Override
    public Set<ClientDTO> getClientsDTO() {
         return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toSet());
    }

    @Override
    public ClientDTO getClientDTO(Long id){
       return new ClientDTO(clientRepository.findById(id).orElse(null));
    }


    @Override
    public Boolean clientExists(String email){
        return clientRepository.findByEmail(email) != null;
    }

    @Override
    public Client getClientAuth(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public Boolean saveClient(Client client) {
        try{
            clientRepository.save(client);
            accountService.createNewAccount(client, AccountType.SAVING);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    // si la account tiene suficiente dinero, devuelve la cuenta, si no devuelve null
    // @Override
    // public Account accountHasBalance(Client client, double amount){
    //     for (Account account : client.getAccounts()) {
    //         if (account.getBalance() >= amount){
    //             return account;
    //         }
    //     } ;
    //     return null;
    // }

    }


