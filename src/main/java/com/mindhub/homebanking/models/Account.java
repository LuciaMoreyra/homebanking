
package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;

import java.util.Set;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Boolean isActive; 
    private AccountType accountType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    public Account(){}

    public Account(String number,AccountType type){
        this.number = number;
        this.creationDate = LocalDateTime.now();
        this.balance = 0;
        this.isActive = true;
        this.accountType = type;
    }


    public AccountType getAccountType(){
        return accountType;
    }
    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

 //   @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void updateBalance(double amount){
        this.balance = this.balance + amount;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        this.updateBalance(transaction.getAmount());
        transactions.add(transaction);
    }
}
