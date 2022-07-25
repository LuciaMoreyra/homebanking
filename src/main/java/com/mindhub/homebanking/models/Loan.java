package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;
    private double maxAmount;
    private double percentage;

    @ElementCollection
    @Column(name="payments")
    private Set<Integer> payments = new HashSet<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();
    

    public Loan(){}
    public Loan(String name, double maxAmount, Set<Integer> payments, double percentage){
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }

    // @JsonIgnore
    public Set<Client> getClients(){
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toSet());
    }
   
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPayments(Set<Integer> payments) {
        this.payments = payments;
    }
    public Set<Integer> getPayments() {
        return payments;
    }
    public void setClientLoans(Set<ClientLoan> clientLoans){
        this.clientLoans = clientLoans;
    }
    public Set<ClientLoan> getClientLoans(){
        return clientLoans;
    }
    public double getPercentage(){
        return percentage;
    }
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }

}
