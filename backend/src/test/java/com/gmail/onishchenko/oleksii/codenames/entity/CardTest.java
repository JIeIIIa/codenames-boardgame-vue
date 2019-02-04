package com.gmail.onishchenko.oleksii.codenames.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class CardTest {
    @Test
    void equals() {
        EqualsVerifier.forClass(Card.class)
                .usingGetClass()
                .withPrefabValues(Room.class, new Room("this", "password"), new Room("another", "password"))
                .verify();
    }
}