package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.entity.*;
import com.gmail.onishchenko.oleksii.codenames.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.exception.CodenamesException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomAlreadyExistsException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.RoomJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.gmail.onishchenko.oleksii.codenames.service.RoomServiceImpl.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    private RoomJpaRepository roomJpaRepository;

    private WordService wordService;

    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        roomJpaRepository = mock(RoomJpaRepository.class);
        wordService = mock(WordService.class);
        roomService = new RoomServiceImpl(roomJpaRepository, wordService);
        roomService.setCoverSupplier(() -> 777);
    }

    @Test
    void findAll() {
        //Given    
        RoomDto firstDto = new RoomDto(7L, "first");
        RoomDto secondDto = new RoomDto(8L, "second");
        Room first = new Room();
        first.setId(7L);
        first.setTitle("first");
        Room second = new Room();
        second.setId(8L);
        second.setTitle("second");
        when(roomJpaRepository.findAll()).thenReturn(asList(first, second));

        //When
        List<RoomDto> result = roomService.findAll();

        //Then
        assertThat(result).hasSize(2)
                .containsExactlyInAnyOrder(firstDto, secondDto);
    }

    @Nested
    class Create {
        private final String TITLE = "roomTitle";
        private RoomDto roomDto;

        @BeforeEach
        void setUp() {
            roomDto = new RoomDto(777L, TITLE);
        }

        @Test
        void roomWithTitleAlreadyExists() {
            //Given
            Room room = new Room(TITLE, "password");
            when(roomJpaRepository.findByTitle(TITLE)).thenReturn(Optional.of(room));

            //When
            assertThrows(RoomAlreadyExistsException.class, () -> roomService.create(roomDto));

            //Then
            verify(roomJpaRepository, times(1)).findByTitle(TITLE);
            verifyNoMoreInteractions(roomJpaRepository, wordService);
        }

        @Test
        void success() {
            //Given
            RoomDto expected = new RoomDto(7L, TITLE);
            when(roomJpaRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
            when(roomJpaRepository.saveAndFlush(any(Room.class)))
                    .thenAnswer(invocationOnMock -> {
                        Room room = (Room) invocationOnMock.getArguments()[0];
                        room.setId(7L);
                        return room;
                    });

            //When
            RoomDto result = roomService.create(this.roomDto);

            //Then
            assertThat(result).isEqualTo(expected);
            verify(roomJpaRepository, times(1)).findByTitle(TITLE);
            verify(roomJpaRepository, times(1)).saveAndFlush(any(Room.class));
            verifyNoMoreInteractions(roomJpaRepository, wordService);
        }
    }

    @Nested
    class NewGame {
        @Nested
        class CreateById {
            @Test
            void roomNotFound() {
                //Given
                when(roomJpaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

                //When
                assertThrows(RoomNotFoundException.class, () -> roomService.newGame(7L));

                //Then
                verify(roomJpaRepository, times(1)).findById(eq(7L));
                verifyNoMoreInteractions(roomJpaRepository, wordService);
            }

            @Test
            void success() {
                //Given
                RoomServiceImpl spy = spy(roomService);
                final Room room = new Room();
                doNothing().when(spy).newGame(any(Room.class));
                when(roomJpaRepository.findById(any(Long.class))).thenReturn(Optional.of(room));

                //When
                spy.newGame(7L);

                //Then
                verify(roomJpaRepository, times(1)).findById(eq(7L));
                verify(spy).newGame(eq(room));
                verifyNoMoreInteractions(roomJpaRepository, wordService);
            }
        }

        @Nested
        class CreateByRoom {
            @Test
            void wordsSizeNotEqualsToRolesSize() {
                //Given
                RoomServiceImpl spy = spy(roomService);
                doReturn(asList(Role.RED, Role.BLUE)).when(spy).generateRoles();
                when(wordService.randomWords(CARDS_COUNT)).thenReturn(asSet(
                        new Word("first"), new Word("second"), new Word("third"))
                );

                //When
                assertThrows(CodenamesException.class, () -> spy.newGame(new Room()));

                //Then
                verify(wordService, times(1)).randomWords(eq(CARDS_COUNT));
                verifyNoMoreInteractions(roomJpaRepository, wordService);
            }

            @Test
            void success() {
                //Given
                Room room = new Room();
                Set<Word> words = IntStream.rangeClosed(1, CARDS_COUNT).boxed()
                        .map(String::valueOf)
                        .map(Word::new)
                        .collect(Collectors.toSet());
                when(wordService.randomWords(CARDS_COUNT)).thenReturn(words);
                when(roomJpaRepository.saveAndFlush(any(Room.class)))
                        .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

                //When
                roomService.newGame(room);

                //Then
                List<Card> cards = room.getCards();
                assertThat(cards).hasSize(CARDS_COUNT);
                assertThat(cards).filteredOn(c -> c.getRole() == Role.RED).hasSize(RED_COUNT);
                assertThat(cards).filteredOn(c -> c.getRole() == Role.BLUE).hasSize(BLUE_COUNT);
                assertThat(cards).filteredOn(c -> c.getRole() == Role.CIVILIAN).hasSize(CIVILIAN_COUNT);
                assertThat(cards).filteredOn(c -> c.getRole() == Role.KILLER).hasSize(KILLER_COUNT);
                assertThat(cards.stream().map(Card::getWord).map(Word::new).collect(Collectors.toList()))
                        .containsExactlyInAnyOrder(words.toArray(new Word[0]));
                assertThat(cards).filteredOn(c -> !c.getSelected()).hasSize(CARDS_COUNT);
                verify(wordService, times(1)).randomWords(eq(CARDS_COUNT));
                verify(roomJpaRepository, times(1)).saveAndFlush(any(Room.class));
                verifyNoMoreInteractions(roomJpaRepository, wordService);
            }
        }
    }

    @Nested
    class SelectWord {
        @Test
        void roomNotFound() {
            //Given
            when(roomJpaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            //When
            assertThrows(RoomNotFoundException.class, () -> roomService.selectWord(7L, 3L));

            //Then
            verify(roomJpaRepository, times(1)).findById(eq(7L));
            verifyNoMoreInteractions(roomJpaRepository, wordService);
        }

        @Test
        void cardNotFound() {
            //Given
            Room room = new Room();
            List<Card> cards = LongStream.rangeClosed(1, CARDS_COUNT).boxed()
                    .map(i -> CardBuilder.getInstance()
                            .id(i).word("word" + i).selected(false)
                            .build()
                    )
                    .peek(room::addCard)
                    .collect(Collectors.toList());

            when(roomJpaRepository.findById(7L)).thenReturn(Optional.of(room));

            //When
            assertThrows(CardNotFoundException.class, () -> roomService.selectWord(7L, 33L));

            //Then
            verify(roomJpaRepository, times(1)).findById(eq(7L));
            verifyNoMoreInteractions(roomJpaRepository, wordService);
        }


        @Test
        void success() {
            //Given
            Room room = new Room();
            List<Card> cards = LongStream.rangeClosed(1, CARDS_COUNT).boxed()
                    .map(i -> CardBuilder.getInstance()
                            .id(i).word("word" + i).selected(false)
                            .build()
                    )
                    .peek(room::addCard)
                    .collect(Collectors.toList());

            when(roomJpaRepository.findById(7L)).thenReturn(Optional.of(room));

            //When
            roomService.selectWord(7L, 3L);

            //Then
            assertThat(room.getCards()).filteredOn(c -> c.getId() == 3L && c.getSelected()).hasSize(1);
            assertThat(room.getCards()).filteredOn(c -> c.getId() != 3L && !c.getSelected()).hasSize(CARDS_COUNT - 1);
            verify(roomJpaRepository, times(1)).findById(eq(7L));
            verifyNoMoreInteractions(roomJpaRepository, wordService);
        }
    }

    @Test
    void generateRoles() {
        //When
        List<Role> roles = roomService.generateRoles();

        //Then
        assertThat(roles).hasSize(CARDS_COUNT);
        assertThat(roles).filteredOn(r -> r == Role.RED).hasSize(RED_COUNT);
        assertThat(roles).filteredOn(r -> r == Role.BLUE).hasSize(BLUE_COUNT);
        assertThat(roles).filteredOn(r -> r == Role.CIVILIAN).hasSize(CIVILIAN_COUNT);
        assertThat(roles).filteredOn(r -> r == Role.KILLER).hasSize(KILLER_COUNT);
    }
}