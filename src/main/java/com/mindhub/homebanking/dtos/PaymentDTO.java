package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Payment;

import java.time.LocalDateTime;

public class PaymentDTO {
  //  private String cardNumber;
    private double amount;
    private String description;
    private LocalDateTime date;
    private String accountNumber;

    public PaymentDTO(){

    }

    public PaymentDTO(Payment payment){
        this.amount = payment.getAmount();
    //    this.cardNumber = payment.getCardNumber();
        this.description = payment.getDescription();
        this.date = payment.getDate();
        this.accountNumber = payment.getAccountNumber();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
