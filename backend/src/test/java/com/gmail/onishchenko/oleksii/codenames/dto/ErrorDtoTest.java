package com.gmail.onishchenko.oleksii.codenames.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorDtoTest {

    static Stream<Arguments> messages() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of("some message", "some message")
        );
    }

    @ParameterizedTest
    @MethodSource(value = {"messages"})
    void setMessage(String message, String expected) {
        //Given
        ErrorDto errorDto = new ErrorDto();

        //When
        errorDto.setMessage(message);

        //Then
        assertThat(errorDto.getMessage()).isEqualTo(expected);
    }

    @Test
    void equals() {
        EqualsVerifier.forClass(ErrorDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .usingGetClass()
                .verify();
    }

}