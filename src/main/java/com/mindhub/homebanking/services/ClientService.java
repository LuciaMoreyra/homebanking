package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ClientService {

    ResponseEntity<Object> getClientsDTO(Authentication authentication);

    ClientDTO getClientDTO(Long id);

    Boolean clientExists(String email);

    Client getClientAuth(Authentication authentication);

    ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password);

}
