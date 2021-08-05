package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private Integer payments;
    private String destinationAccountNumber;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(Long loanId, double amount , Integer payments, String destinationAccountNumber){
        this.amount = amount;
        this.destinationAccountNumber = destinationAccountNumber;
        this.loanId = loanId;
        this.payments = payments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
