package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CardUtilsTests {

    @Test
    void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
    @Test
    void randomNumberIsCreated(){
        int exampleNumber = CardUtils.getRandomNumber(1,344);
        assertThat(exampleNumber, is(not(nullValue())));
    }
    @Test
    void randomNumberIsGreaterOrEqualToMinParameter(){
        int exampleNumber = CardUtils.getRandomNumber(5,10);
        assertThat(exampleNumber, greaterThanOrEqualTo(5));
    }
    @Test
    void cvvNumberHasMaxThreeDigits(){
        int cvv = CardUtils.getCvv();
        assertThat(cvv, not(greaterThan(999)));
    }
}



