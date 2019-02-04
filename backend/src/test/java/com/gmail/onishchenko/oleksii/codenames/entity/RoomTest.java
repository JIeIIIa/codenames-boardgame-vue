package com.gmail.onishchenko.oleksii.codenames.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    @Test
    void removeAllCards() {
        //Given
        Room room = new Room();
        List<Card> cards = asList(
                CardBuilder.getInstance().word("word_1").build(),
                CardBuilder.getInstance().word("word_2").build(),
                CardBuilder.getInstance().word("word_3").build()
        );
        for (Card card : cards) {
            room.addCard(card);
        }

        //When
        room.removeAllCards();

        //Then
        assertThat(room.getCards()).hasSize(0);
        assertThat(cards).filteredOn(card -> card.getRoom() == null).hasSize(3);
    }

    @Test
    void equals() {
        EqualsVerifier.forClass(Room.class)
                .usingGetClass()
                .withPrefabValues(Card.class,
                        CardBuilder.getInstance().word("word").build(),
                        CardBuilder.getInstance().word("another").build()
                )
                .verify();
    }
}