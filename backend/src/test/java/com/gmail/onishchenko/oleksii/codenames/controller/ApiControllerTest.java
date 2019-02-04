package com.gmail.onishchenko.oleksii.codenames.controller;

import com.gmail.onishchenko.oleksii.codenames.dto.CardDto;
import com.gmail.onishchenko.oleksii.codenames.dto.CardDtoBuilder;
import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.entity.Role;
import com.gmail.onishchenko.oleksii.codenames.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.exception.CodenamesException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomAlreadyExistsException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ApiController.class, AdviceController.class})
class ApiControllerTest {

    private static final String BASE_URL = "/api/v1";

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        reset(roomService);
    }

    @Test
    void findAll() throws Exception {
        //Given
        RoomDto first = new RoomDto(7L, "first");
        RoomDto second = new RoomDto(8L, "second");
        second.setPassword("top-secret");
        when(roomService.findAll()).thenReturn(asList(first, second));

        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(BASE_URL + "/rooms");

        //When
        ResultActions perform = mockMvc.perform(get);

        //Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].title").value("first"))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[1].id", is(8)))
                .andExpect(jsonPath("$[1].title", is("second")))
                .andExpect(jsonPath("$[1].password").doesNotExist());
    }

    @Nested
    class CreateRoom {
        private RoomDto roomDto;

        private MockHttpServletRequestBuilder post;

        @BeforeEach
        void setUp() {
            roomDto = new RoomDto();
            roomDto.setTitle("newRoom");
            roomDto.setPassword("qwerty");
            post = MockMvcRequestBuilders.post(BASE_URL + "/rooms")
                    .flashAttr("room", roomDto);
        }

        @Test
        void roomAlreadyExists() throws Exception {
            //Given
            when(roomService.create(roomDto)).thenThrow(RoomAlreadyExistsException.class);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").isString());
        }

        @Test
        void success() throws Exception {
            //Given
            RoomDto createdRoom = new RoomDto(7L, "newRoom");
            createdRoom.setPassword("qwerty");

            when(roomService.create(roomDto)).thenReturn(createdRoom);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("id").value(7))
                    .andExpect(jsonPath("title").value("newRoom"))
                    .andExpect(jsonPath("password").doesNotExist());
        }
    }

    @Nested
    class RetrieveCards {
        private MockHttpServletRequestBuilder get;
        private final Long ROOM_ID = 7L;
        @BeforeEach
        void setUp() {
            get = MockMvcRequestBuilders.get(BASE_URL + "/rooms/{roomId}/cards", ROOM_ID);
        }

        @Test
        void roomAlreadyExists() throws Exception {
            //Given
            when(roomService.retrieveCards(ROOM_ID)).thenThrow(RoomNotFoundException.class);

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").isString());
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto first = CardDtoBuilder.getInstance()
                    .id(777L).word("word_1").role(Role.BLUE).selected(false).cover(2).build();
            CardDto second = CardDtoBuilder.getInstance()
                    .id(123L).word("word_2").role(Role.KILLER).selected(true).cover(11).build();
            when(roomService.retrieveCards(ROOM_ID)).thenReturn(asList(first, second));

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(777))
                    .andExpect(jsonPath("$[0].word").value("word_1"))
                    .andExpect(jsonPath("$[0].role").value("BLUE"))
                    .andExpect(jsonPath("$[0].selected").value(false))
                    .andExpect(jsonPath("$[0].cover").value(2))
                    .andExpect(jsonPath("$[1].id").value(123))
                    .andExpect(jsonPath("$[1].word").value("word_2"))
                    .andExpect(jsonPath("$[1].role").value("KILLER"))
                    .andExpect(jsonPath("$[1].selected").value(true))
                    .andExpect(jsonPath("$[1].cover").value(11));
        }
    }

    @Nested
    class NewGame {
        private MockHttpServletRequestBuilder post;
        private final Long ROOM_ID = 7L;
        @BeforeEach
        void setUp() {
            post = MockMvcRequestBuilders.post(BASE_URL + "/rooms/{roomId}/cards", ROOM_ID);
        }

        @Test
        void roomAlreadyExists() throws Exception {
            //Given
            doThrow(CodenamesException.class).when(roomService).newGame(ROOM_ID);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").isString());
        }

        @Test
        void success() throws Exception {
            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isOk());
        }
    }

    @Nested
    class SelectCard {
        private MockHttpServletRequestBuilder put;
        private final Long ROOM_ID = 7L;
        private final Long CARD_ID = 123L;
        @BeforeEach
        void setUp() {
            put = MockMvcRequestBuilders.put(
                    BASE_URL + "/rooms/{roomId}/cards/{cardId}", ROOM_ID, CARD_ID);
        }

        @ParameterizedTest
        @ValueSource(classes = {RoomNotFoundException.class, CardNotFoundException.class})
        void failure(Class<? extends CodenamesException> clazz) throws Exception {
            //Given
            doThrow(clazz).when(roomService).selectCard(ROOM_ID, CARD_ID);

            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").isString());
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto cardDto = CardDtoBuilder.getInstance()
                    .id(777L).word("word_1").role(Role.BLUE).selected(false).cover(2).build();
            when(roomService.selectCard(ROOM_ID, CARD_ID)).thenReturn(cardDto);

            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath(".id").value(777))
                    .andExpect(jsonPath(".word").value("word_1"))
                    .andExpect(jsonPath(".role").value("BLUE"))
                    .andExpect(jsonPath(".selected").value(false))
                    .andExpect(jsonPath(".cover").value(2));

        }
    }
}