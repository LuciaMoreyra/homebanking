package com.mindhub.homebanking.repositories;

import java.util.Set;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
public interface AccountRepository  extends JpaRepository<Account, Long>{

    Account findByNumber(String number);
    
    Set<Account> findByClientId(Long clientId);

    Set<Account> findByClientIdAndBalanceGreaterThan(Long clientId, double balance);
}
