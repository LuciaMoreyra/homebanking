package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;
// import java.util.Random;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    Set<Payment> payments = new HashSet<>();

    private CardType type;
    private CardColor color;

    private String number;
    private String cardholder;
    private int cvv;

    private LocalDateTime thruDate;
    private LocalDateTime fromDate;

    public Card(){}

    public Card(CardType type, CardColor color,  Client client, String number, int cvv){
        this.type = type;
        this.color = color;
        this.thruDate = LocalDateTime.now().plusYears(5);
        this.fromDate = LocalDateTime.now();
        this.client = client;
        this.cardholder = client.getFirstName() + " " + client.getLastName();
        this.cvv = cvv;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    public Set<Payment> getPayments() {
        return payments;
    }
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
