package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;


import java.util.Set;

public interface ClientService {

    Set<ClientDTO> getClientsDTO();

    ClientDTO getClientDTO(Long id);

    Boolean clientExists(String email);

    Client getClientAuth(Authentication authentication);

    Boolean saveClient(Client client);
 
    // Account accountHasBalance(Client client, double amount);
}
