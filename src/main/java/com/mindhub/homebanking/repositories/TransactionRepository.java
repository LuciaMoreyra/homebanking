package com.mindhub.homebanking.repositories;

import java.time.LocalDateTime;
import java.util.Set;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    Set<Transaction> findByAccountId(Long accountId);

   Set<Transaction>  findByAccountIdAndDateBetween(Long accountId, LocalDateTime start, LocalDateTime end);
}
