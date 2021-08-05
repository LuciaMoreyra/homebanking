package com.mindhub.homebanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long>{
    public Loan findByName(String name);
    public Boolean existsLoanByName(String name);

}
