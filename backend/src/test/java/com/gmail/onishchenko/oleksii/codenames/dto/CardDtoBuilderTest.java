package com.gmail.onishchenko.oleksii.codenames.dto;

import com.gmail.onishchenko.oleksii.codenames.entity.Role;
import com.gmail.onishchenko.oleksii.codenames.source.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class CardDtoBuilderTest implements DataSource {

    @ParameterizedTest(name = "[{index}] ==> id = ''{0}''")
    @MethodSource(value = {"longs"})
    void id(Long id) {
        //When
        CardDto cardDto = CardDtoBuilder.getInstance().id(id).build();

        //Then
        assertThat(cardDto.getId()).isEqualTo(id);
        assertThat(cardDto).isEqualToIgnoringGivenFields(new CardDto(), "id");
    }

    @ParameterizedTest(name = "[{index}] ==> word = ''{0}''")
    @MethodSource(value = {"strings"})
    void word(String word) {
        //When
        CardDto cardDto = CardDtoBuilder.getInstance().word(word).build();

        //Then
        assertThat(cardDto.getWord()).isEqualTo(word);
        assertThat(cardDto).isEqualToIgnoringGivenFields(new CardDto(), "word");
    }

    @ParameterizedTest(name = "[{index}] ==> roles = ''{0}''")
    @MethodSource(value = {"roles"})
    void role(Role role) {
        //When
        CardDto cardDto = CardDtoBuilder.getInstance().role(role).build();

        //Then
        assertThat(cardDto.getRole()).isEqualTo(role);
        assertThat(cardDto).isEqualToIgnoringGivenFields(new CardDto(), "role");
    }

    @Test
    void selected() {
        //When
        CardDto cardDto = CardDtoBuilder.getInstance().selected(true).build();

        //Then
        assertThat(cardDto.getSelected()).isTrue();
        assertThat(cardDto).isEqualToIgnoringGivenFields(new CardDto(), "selected");
    }

    @ParameterizedTest(name = "[{index}] ==> cover = ''{0}''")
    @MethodSource(value = {"integers"})
    void cover(Integer cover) {
        //When
        CardDto cardDto = CardDtoBuilder.getInstance().cover(cover).build();

        //Then
        assertThat(cardDto.getCover()).isEqualTo(cover);
        assertThat(cardDto).isEqualToIgnoringGivenFields(new CardDto(), "cover");
    }
}