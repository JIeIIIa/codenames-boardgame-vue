package com.gmail.onishchenko.oleksii.codenames.dto;

import com.gmail.onishchenko.oleksii.codenames.entity.Card;
import com.gmail.onishchenko.oleksii.codenames.entity.CardBuilder;
import com.gmail.onishchenko.oleksii.codenames.entity.Role;
import com.gmail.onishchenko.oleksii.codenames.entity.Room;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardDtoTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(RoomDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .usingGetClass()
                .verify();
    }

    @Test
    void toDto() {
        //Given
        Room room = new Room("qwerty", "password");
        room.setId(7L);
        Card card = CardBuilder.getInstance().id(101L).word("word_1").role(Role.RED).selected(true).cover(2).build();
        room.addCard(card);

        //When
        CardDto result = CardDto.toDto(card);

        //Then
        assertThat(result.getId()).isEqualTo(101L);
        assertThat(result.getWord()).isEqualTo("word_1");
        assertThat(result.getRole()).isEqualTo(Role.RED);
        assertThat(result.getSelected()).isTrue();
        assertThat(result.getCover()).isEqualTo(2);
    }
}