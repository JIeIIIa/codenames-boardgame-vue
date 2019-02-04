package com.gmail.onishchenko.oleksii.codenames.dto;

import com.gmail.onishchenko.oleksii.codenames.entity.Room;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomDtoTest {

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
        final Long id = 777L;
        final String title = "awesomeTitle";
        Room room = new Room();
        room.setId(id);
        room.setTitle(title);
        RoomDto expected = new RoomDto(id, title);

        //When
        RoomDto roomDto = RoomDto.toDto(room);

        //Then
        assertThat(roomDto).isEqualTo(expected);
    }
}