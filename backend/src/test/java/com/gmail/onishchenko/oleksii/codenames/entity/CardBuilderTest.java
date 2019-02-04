package com.gmail.onishchenko.oleksii.codenames.entity;

import com.gmail.onishchenko.oleksii.codenames.source.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class CardBuilderTest implements DataSource {

    @ParameterizedTest(name = "[{index}] ==> id = ''{0}''")
    @MethodSource(value = {"longs"})
    void id(Long id) {
        //When
        Card card = CardBuilder.getInstance().id(id).build();

        //Then
        assertThat(card.getId()).isEqualTo(id);
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "id");
    }

    @ParameterizedTest(name = "[{index}] ==> word = ''{0}''")
    @MethodSource(value = {"strings"})
    void word(String word) {
        //When
        Card card = CardBuilder.getInstance().word(word).build();

        //Then
        assertThat(card.getWord()).isEqualTo(word);
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "word");
    }

    @ParameterizedTest(name = "[{index}] ==> roles = ''{0}''")
    @MethodSource(value = {"roles"})
    void role(Role role) {
        //When
        Card card = CardBuilder.getInstance().role(role).build();

        //Then
        assertThat(card.getRole()).isEqualTo(role);
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "role");
    }

    @Test
    void selected() {
        //When
        Card card = CardBuilder.getInstance().selected(true).build();

        //Then
        assertThat(card.getSelected()).isTrue();
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "selected");
    }

    @ParameterizedTest(name = "[{index}] ==> cover = ''{0}''")
    @MethodSource(value = {"integers"})
    void cover(Integer cover) {
        //When
        Card card = CardBuilder.getInstance().cover(cover).build();

        //Then
        assertThat(card.getCover()).isEqualTo(cover);
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "cover");
    }

    @Test
    void room() {
        //Given
        final Room room = new Room();

        //When
        Card card = CardBuilder.getInstance().room(room).build();

        //Then
        assertThat(card.getRoom()).isEqualTo(room);
        assertThat(card).isEqualToIgnoringGivenFields(new Card(), "room");
    }
}