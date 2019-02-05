package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.dto.CardDto;
import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.entity.Room;

import java.util.List;

public interface RoomService {

    List<RoomDto> findAll();

    List<Room> findAllAsRooms();

    RoomDto create(RoomDto roomDto);

    void newGame(Long roomId);

    CardDto selectCard(Long roomId, Long cardId);

    List<CardDto> retrieveCards(Long roomId);

    void deleteRoomById(Long roomId);
}
