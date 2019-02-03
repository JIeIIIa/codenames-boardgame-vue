package com.gmail.onishchenko.oleksii.codenames.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.gmail.onishchenko.oleksii.codenames.configuration.DBUnitConfiguration;
import com.gmail.onishchenko.oleksii.codenames.entity.Word;
import com.gmail.onishchenko.oleksii.codenames.exception.WordNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.WordJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {DBUnitConfiguration.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = "classpath:datasets/WordJpaRepository/init_dataset.xml")
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WordServiceImplIT {
    @Autowired
    private WordJpaRepository wordJpaRepository;

    private WordServiceImpl wordService;

    @BeforeEach
    void setUp() {
        wordService = new WordServiceImpl(wordJpaRepository);
    }

    @Test
    void randomWordsWhenNotEnoughWordsInDatabase() {
        //When
        assertThrows(WordNotFoundException.class, () -> wordService.randomWords(10));

    }

    @Test
    void randomWordsSuccess() {
        //When
        Set<Word> word = wordService.randomWords(2);

        //Then
        assertThat(word).isNotNull();
        assertThat(word).hasSize(2)
                .containsAnyOf(
                        new Word("first"),
                        new Word("second"),
                        new Word("third")
                );
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/WordJpaRepository/after_inserted_dataset.xml")
    void insertSuccess() {
        //When
        boolean result = wordService.insert("newWord");

        //Then
        assertThat(result).isTrue();
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/WordJpaRepository/init_dataset.xml")
    void insertWhenWordAlreadyExists() {
        //When
        boolean result = wordService.insert("second");

        //Then
        assertThat(result).isFalse();
        wordJpaRepository.flush();
    }

}
