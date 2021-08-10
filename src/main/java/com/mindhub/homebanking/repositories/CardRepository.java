package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    Boolean existsCardById(Long cardId);

    Set<Card> findByClientIdAndType(Long clientId, CardType type);

    Card findByNumber(String number);
}

