package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomService {
    @Transactional
    List<RoomDto> findAll();

    RoomDto create(RoomDto roomDto);

    void newGame(Long roomId);

    void selectWord(Long roomId, Long cardId);
}
