package com.mindhub.homebanking.services.implemets;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> getClientsDTO(Authentication authentication) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ADMIN")){
           Set<ClientDTO> clientList = clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toSet());
            return new ResponseEntity<>(clientList, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("admin role is required",HttpStatus.FORBIDDEN);
        }

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
    public ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (Boolean.TRUE.equals(clientExists(email))) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);
        accountService.createNewAccount(client, AccountType.SAVING);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    }


