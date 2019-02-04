package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.entity.*;
import com.gmail.onishchenko.oleksii.codenames.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.exception.CodenamesException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomAlreadyExistsException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.RoomJpaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Collections.nCopies;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LogManager.getLogger(RoomServiceImpl.class);
    static final int CARDS_COUNT = 25;
    static final int RED_COUNT = 9;
    static final int BLUE_COUNT = 8;
    static final int CIVILIAN_COUNT = 7;
    static final int KILLER_COUNT = 1;

    private final RoomJpaRepository roomJpaRepository;
    private final WordService wordService;

    private Supplier<Integer> coverSupplier;

    @Autowired
    public RoomServiceImpl(RoomJpaRepository roomJpaRepository,
                           WordService wordService) {
        log.info("Create instance of {}", RoomServiceImpl.class.getName());
        this.roomJpaRepository = roomJpaRepository;
        this.wordService = wordService;

        coverSupplier = () -> ThreadLocalRandom.current().nextInt(1, 3);
    }

    void setCoverSupplier(Supplier<Integer> coverSupplier) {
        this.coverSupplier = coverSupplier;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomDto> findAll() {
        return roomJpaRepository.findAll()
                .stream()
                .map(RoomDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto create(RoomDto roomDto) {
        roomJpaRepository.findByTitle(roomDto.getTitle())
                .ifPresent(r -> {
                    throw new RoomAlreadyExistsException("Room with title = '" + r.getTitle() + "' already exists");
                });
        Room room = roomJpaRepository.saveAndFlush(
                new Room(roomDto.getTitle(), roomDto.getPassword())
        );

        return RoomDto.toDto(room);
    }

    @Override
    public void newGame(Long roomId) {
        roomJpaRepository.findById(roomId)
                .map(r -> {
                    newGame(r);
                    return r;
                })
                .orElseThrow(() -> new RoomNotFoundException("Room with id = " + roomId + " not found"));
    }

    void newGame(Room room) {
        List<Role> roles = generateRoles();
        List<Word> words = new ArrayList<>(wordService.randomWords(CARDS_COUNT));

        if (roles.size() != words.size()) {
            throw new CodenamesException("roles.size() not equals to words.size()");
        }

        room.getCards().forEach(room::removeCard);

        for (int i = 0; i < words.size(); i++) {
            Card card = CardBuilder.getInstance()
                    .word(words.get(i).getContent())
                    .role(roles.get(i))
                    .selected(false)
                    .cover(coverSupplier.get())
                    .room(room)
                    .build();
            room.addCard(card);
        }
        roomJpaRepository.saveAndFlush(room);
    }

    @Override
    public void selectWord(Long roomId, Long cardId) {
        Room room = roomJpaRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id = " + roomId + " not found"));
        Card card = room.getCards().stream().filter(c -> c.getId().equals(cardId)).findFirst()
                .orElseThrow(() -> new CardNotFoundException("Card with id = " + cardId + " not found"));
        card.setSelected(true);
    }

    List<Role> generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.addAll(nCopies(RED_COUNT, Role.RED));
        roles.addAll(nCopies(BLUE_COUNT, Role.BLUE));
        roles.addAll(nCopies(CIVILIAN_COUNT, Role.CIVILIAN));
        roles.addAll(nCopies(KILLER_COUNT, Role.KILLER));

        Collections.shuffle(roles);
        return roles;
    }
}
