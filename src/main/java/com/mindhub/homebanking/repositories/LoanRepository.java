package com.mindhub.homebanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mindhub.homebanking.models.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{
    
}
