package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.HashSet;
import java.util.Set;

public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private Set<Integer> payments = new HashSet<>();
    private double percentage;

    public LoanDTO(){}

    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.percentage = loan.getPercentage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }

    public void setPayments(Set<Integer> payments) {
        this.payments = payments;
    }
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }
    public double getPercentage() {
        return percentage;
    }
}
