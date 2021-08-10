package com.mindhub.homebanking.services.implemets;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService{
    
    @Autowired 
    LoanRepository loanRepository;

    @Override
    public Boolean loanExists(String name){
        return loanRepository.existsLoanByName(name);
    }

    @Override
    public ResponseEntity<Object> createLoan(LoanDTO loanDTO){
        if (loanDTO.getName().isEmpty() || loanDTO.getMaxAmount() <= 0 || loanDTO.getPayments().isEmpty()
                || loanDTO.getPercentage() <= 0) {
            return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
        }

        if (loanRepository.existsLoanByName(loanDTO.getName())){
            return new ResponseEntity<>("this loan already exists", HttpStatus.FORBIDDEN);
        }

        try {
            Loan newLoan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(),
                    loanDTO.getPercentage());
            loanRepository.save(newLoan);
            return new ResponseEntity<>("created", HttpStatus.CREATED);
        } catch (Exception e) {
            // en el caso de que haya un error
           return new ResponseEntity<>(e, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Set<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet());
    }
    @Override
    public LoanDTO getLoan(Long id){
        Optional<Loan> loan = loanRepository.findById(id);
        if (loan.isEmpty()){
            return null;
        }
        return new LoanDTO(loan.get());
    }
    
}
