package com.gmail.onishchenko.oleksii.codenames.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(Word.class)
                .suppress(Warning.SURROGATE_KEY)
                .usingGetClass()
                .verify();
    }
}