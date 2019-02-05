package com.gmail.onishchenko.oleksii.codenames.configuration;

import com.gmail.onishchenko.oleksii.codenames.entity.Room;
import com.gmail.onishchenko.oleksii.codenames.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

class ScheduledTasksTest {

    private RoomService roomService;

    private ScheduledTasks scheduledTasks;

    @BeforeEach
    void setUp() {
        roomService = mock(RoomService.class);
        scheduledTasks = new ScheduledTasks(roomService);
        scheduledTasks.setLifetime(30);
    }

    @Test
    void deleteOldRoom() {
        //Given
        final Room first = new Room();
        first.setId(7L);
        first.setTitle("first");
        first.setDateModified(LocalDateTime.now().minus(31, ChronoUnit.MINUTES));
        final Room second = new Room();
        second.setId(8L);
        second.setTitle("second");
        second.setDateModified(LocalDateTime.now());
        final Room third = new Room();
        third.setId(9L);
        third.setTitle("third");
        third.setDateModified(LocalDateTime.now().minus(29, ChronoUnit.MINUTES));
        final Room fourth = new Room();
        fourth.setId(11L);

        when(roomService.findAllAsRooms()).thenReturn(asList(first, second, third, fourth));

        //When
        scheduledTasks.deleteOldRooms();

        //Then
        verify(roomService, times(1)).findAllAsRooms();
        verify(roomService, times(1)).deleteRoomById(7L);
        verify(roomService, times(1)).deleteRoomById(11L);
        verifyNoMoreInteractions(roomService);
    }
}