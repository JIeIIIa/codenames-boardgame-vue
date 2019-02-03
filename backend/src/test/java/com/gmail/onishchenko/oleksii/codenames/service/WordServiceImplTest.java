package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.entity.Word;
import com.gmail.onishchenko.oleksii.codenames.exception.WordNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.WordJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WordServiceImplTest {

    @MockBean
    private WordJpaRepository wordJpaRepository;

    private WordServiceImpl wordService;

    @BeforeEach
    void setUp() {
        reset(wordJpaRepository);
        wordService = new WordServiceImpl(wordJpaRepository);
    }
  
    @Nested
    class RandomWord {
        @Test
        void noWordInRepository() {
            //Given    
            when(wordJpaRepository.count()).thenReturn(0L);

            //When
            assertThrows(WordNotFoundException.class, () -> wordService.randomWords(1));

            //Then
            verify(wordJpaRepository, times(1)).count();
            verifyNoMoreInteractions(wordJpaRepository);
        }

        @Test
        @SuppressWarnings("unchecked")
        void noContentInPageResult() {
            //Given
            Page<Word> page = (Page<Word>) mock(Page.class);
            when(page.hasContent()).thenReturn(false);
            when(wordJpaRepository.count()).thenReturn(1L);
            when(wordJpaRepository.findAll(any(PageRequest.class))).thenReturn(page);

            //When
            assertThrows(WordNotFoundException.class, () -> wordService.randomWords(1));

            //Then
            verify(wordJpaRepository, times(1)).count();
            verify(wordJpaRepository, times(1)).findAll(any(PageRequest.class));
            verifyNoMoreInteractions(wordJpaRepository);
        }

        @Test
        @SuppressWarnings("unchecked")
        void success() {
            //Given
            String expected = "someWord";
            Page<Word> page = (Page<Word>) mock(Page.class);
            when(page.hasContent()).thenReturn(true);
            when(wordJpaRepository.count()).thenReturn(1L);
            when(wordJpaRepository.findAll(any(PageRequest.class))).thenReturn(page);
            when(page.getContent()).thenReturn(singletonList(new Word(expected)));

            //When
            Set<Word> word = wordService.randomWords(1);

            //Then
            assertThat(word).containsExactly(new Word(expected));
            verify(wordJpaRepository, times(1)).count();
            verify(wordJpaRepository, times(1)).findAll(any(PageRequest.class));
            verifyNoMoreInteractions(wordJpaRepository);
        }
    }

    @Nested
    class Insert {
        private final String CONTENT = "someWord";

        @Test
        void wordAlreadyExists() {
            //Given
            when(wordJpaRepository.findByContent(CONTENT)).thenReturn(Optional.of(new Word(CONTENT)));

            //When
            boolean result = wordService.insert(CONTENT);

            //Then
            assertThat(result).isFalse();
            verify(wordJpaRepository, times(1)).findByContent(eq(CONTENT));
            verifyNoMoreInteractions(wordJpaRepository);
        }

        @Test
        void success() {
            //Given
            when(wordJpaRepository.findByContent(CONTENT)).thenReturn(Optional.empty());

            //When
            boolean result = wordService.insert(CONTENT);

            //Then
            assertThat(result).isTrue();
            verify(wordJpaRepository, times(1)).findByContent(eq(CONTENT));
            verify(wordJpaRepository, times(1)).saveAndFlush(eq(new Word(CONTENT)));
            verifyNoMoreInteractions(wordJpaRepository);
        }
    }
}