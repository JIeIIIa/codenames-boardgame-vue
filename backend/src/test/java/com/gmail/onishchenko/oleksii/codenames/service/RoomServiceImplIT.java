package com.gmail.onishchenko.oleksii.codenames.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.gmail.onishchenko.oleksii.codenames.configuration.DBUnitConfiguration;
import com.gmail.onishchenko.oleksii.codenames.dto.CardDto;
import com.gmail.onishchenko.oleksii.codenames.dto.RoomDto;
import com.gmail.onishchenko.oleksii.codenames.entity.Role;
import com.gmail.onishchenko.oleksii.codenames.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomAlreadyExistsException;
import com.gmail.onishchenko.oleksii.codenames.exception.RoomNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.RoomJpaRepository;
import org.dbunit.database.IDatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ContextConfiguration(classes = {DBUnitConfiguration.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = "classpath:datasets/RoomService/init_dataset.xml")
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomServiceImplIT {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("integration-tests-db");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private IDatabaseConnection connection;

    @Autowired
    private RoomJpaRepository roomJpaRepository;

    @Autowired
    private WordService wordService;

    private RoomServiceImpl instance;

    @BeforeEach
    void setUp() throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        statement.execute("alter sequence  room_id_seq RESTART WITH 1");
        statement.execute("alter sequence  card_id_seq RESTART WITH 1");

        instance = new RoomServiceImpl(roomJpaRepository, wordService);
        instance.setCoverSupplier(() -> 777);
    }

    @Test
    void findAll() {
        //Given
        RoomDto expected = new RoomDto(-10L, "someRoom");

        //When
        List<RoomDto> result = instance.findAll();

        //Then
        assertThat(result).hasSize(1)
                .containsExactly(expected);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/RoomService/room_created_dataset.xml")
    void createSuccess() {
        //Given
        RoomDto roomDto = new RoomDto(100L, "newRoom");
        roomDto.setPassword("secret-password");
        RoomDto expected = new RoomDto(1L, "newRoom");
        expected.setPassword(null);

        //When
        RoomDto result = instance.create(roomDto);

        //Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void createWhenRoomAlreadyExists() {
        //Given
        RoomDto roomDto = new RoomDto(100L, "someRoom");
        roomDto.setPassword("secret-password");

        //When
        assertThrows(RoomAlreadyExistsException.class, () -> instance.create(roomDto));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/RoomService/new_game_dataset.xml")
    void newGameSuccess() {
        //When
        instance.newGame(-10L);
    }

    @Test
    void newGameWhenRoomNotFound() {
        //When
        assertThrows(RoomNotFoundException.class, () -> instance.newGame(123L));
    }

    @Test
    void selectCardWhenRoomNotFound() {
        //When
        assertThrows(RoomNotFoundException.class, () -> instance.selectCard(123L, 7L));
    }

    @Test
    void selectCardWhenWordNotFound() {
        //When
        assertThrows(CardNotFoundException.class, () -> instance.selectCard(-10L, 7L));
    }

    @Test
    @DatabaseSetup(value = "classpath:datasets/RoomService/select_word_before_dataset.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/RoomService/select_word_after_dataset.xml")
    void selectCardSuccess() {
        //Given
        CardDto expected = new CardDto();
        expected.setId(7L);
        expected.setWord("awesome");
        expected.setSelected(true);
        expected.setRole(Role.BLUE);
        expected.setCover(2);

        //When
        CardDto result = instance.selectCard(-10L, 7L);

        //Then
        assertThat(result).isEqualTo(expected);
        roomJpaRepository.flush();
    }

    @Test
    void retrieveCardsWhenRoomNotFound() {
        //When
        assertThrows(RoomNotFoundException.class, () -> instance.retrieveCards(7L));
    }

    @Test
    void retrieveCardsWhenRoomHasNoWord() {
        //When
        List<CardDto> result = instance.retrieveCards(-10L);

        //Then
        assertThat(result).hasSize(0);
    }

    @Test
    @DatabaseSetup(value = "classpath:datasets/RoomService/room_with_cards_dataset.xml")
    void retrieveCardsSuccess() {
        //Given
        final long roomId = -10;

        CardDto expectedCardDto = new CardDto();
        expectedCardDto.setId(7L);
        expectedCardDto.setWord("word_1");
        expectedCardDto.setRole(Role.BLUE);
        expectedCardDto.setSelected(true);
        expectedCardDto.setCover(2);

        CardDto expectedAnotherCardDto = new CardDto();
        expectedAnotherCardDto.setId(8L);
        expectedAnotherCardDto.setWord("word_4");
        expectedAnotherCardDto.setRole(Role.CIVILIAN);
        expectedAnotherCardDto.setSelected(false);
        expectedAnotherCardDto.setCover(1);

        //When
        List<CardDto> result = instance.retrieveCards(roomId);

        //Then
        assertThat(result).hasSize(2).containsOnlyOnce(expectedCardDto, expectedAnotherCardDto);
    }

    @Test
    @DatabaseSetup(value = "classpath:datasets/RoomService/room_with_cards_dataset.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/RoomService/room_with_cards_after_deleted_dataset.xml")
    void deleteRoomSuccess() {
        //When
        instance.deleteRoomById(-10L);
    }
}
