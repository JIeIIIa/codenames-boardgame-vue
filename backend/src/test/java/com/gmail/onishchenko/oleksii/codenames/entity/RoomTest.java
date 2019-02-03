package com.gmail.onishchenko.oleksii.codenames.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class RoomTest {

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