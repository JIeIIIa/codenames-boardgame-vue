package com.gmail.onishchenko.oleksii.codenames.controller;

import com.gmail.onishchenko.oleksii.codenames.dto.CardDto;
import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.service.RoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private static final Logger log = LogManager.getLogger(ApiController.class);

    private final RoomService roomService;

    @Autowired
    public ApiController(RoomService roomService) {
        log.info("Create instance of {}", ApiController.class.getName());
        this.roomService = roomService;
    }

    @GetMapping(value = "/rooms")
    public ResponseEntity<List<RoomDto>> roomsList() {
        log.traceEntry();
        List<RoomDto> rooms = roomService.findAll();

        return ResponseEntity.ok(rooms);
    }

    @PostMapping(value = "/rooms")
    public ResponseEntity<RoomDto> createRoom(@ModelAttribute("room") RoomDto roomDto) {
        log.traceEntry("Try to create room: {}", roomDto.getTitle());
        RoomDto createdRoom = roomService.create(roomDto);
        log.debug("Room {} was created", createdRoom);

        return ResponseEntity
                .created(URI.create("/api/v1/rooms/" + createdRoom.getId()))
                .body(createdRoom);
    }

    @GetMapping(value = "/rooms/{roomId}/cards")
    public ResponseEntity<List<CardDto>> retrieveCards(
            @PathVariable("roomId") Long roomId) {
        log.traceEntry("Retrieve cards for the room with id = {}", roomId);
        List<CardDto> cards = roomService.retrieveCards(roomId);

        return ResponseEntity.ok(cards);
    }

    @PostMapping(value = "/rooms/{roomId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public void newGame(
            @PathVariable("roomId") Long roomId) {
        log.traceEntry("Create new game for the room with id = {}", roomId);
        roomService.newGame(roomId);
    }

    @PutMapping(value = "/rooms/{roomId}/cards/{cardId}")
    public ResponseEntity<CardDto> selectCard(
            @PathVariable("roomId") Long roomId,
            @PathVariable("cardId") Long cardId) {
        log.traceEntry("Select the word on the card with id = {} of the room with id = {}", cardId, roomId);
        CardDto cardDto = roomService.selectCard(roomId, cardId);

        return ResponseEntity.ok(cardDto);
    }
}
