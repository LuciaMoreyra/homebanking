package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDateTime;



public class CardDTO{
    private Long id;
    private String cardHolder;

    private CardType type;
    private CardColor color;

    private String number;
    private int cvv;

    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    public CardDTO(){}

    public CardDTO(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardholder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public CardColor getColor() {
        return color;
    }
    public void setColor(CardColor color) {
        this.color = color;
    }
    public int getCvv() {
        return cvv;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    public LocalDateTime getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }
    public void setType(CardType type) {
        this.type = type;
    }
    public CardType getType() {
        return type;
    }


}