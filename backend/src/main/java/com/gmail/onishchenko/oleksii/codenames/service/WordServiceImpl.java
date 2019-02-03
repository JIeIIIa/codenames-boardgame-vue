package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.entity.Word;
import com.gmail.onishchenko.oleksii.codenames.exception.WordNotFoundException;
import com.gmail.onishchenko.oleksii.codenames.repository.WordJpaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class WordServiceImpl implements WordService {

    private static final Logger log = LogManager.getLogger(WordServiceImpl.class);

    private final WordJpaRepository wordJpaRepository;

    @Autowired
    public WordServiceImpl(WordJpaRepository wordJpaRepository) {
        log.info("Create instance of {}", WordServiceImpl.class.getName());
        this.wordJpaRepository = wordJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Word> randomWords(int size) {
        int count = (int) wordJpaRepository.count();
        if (count < size) {
            throw new WordNotFoundException("Not enough words to make a set");
        }

        return ThreadLocalRandom.current().ints(0, count)
                .distinct()
                .limit(size)
                .boxed()
                .map(i -> wordJpaRepository.findAll(PageRequest.of(i, 1)))
                .peek(page -> {
                    if (!page.hasContent()) {
                        throw new WordNotFoundException();
                    }
                })
                .map(Slice::getContent)
                .map(list -> list.get(0))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean insert(String content) {
        Optional<Word> word = wordJpaRepository.findByContent(content);
        if (word.isPresent()) {
            return false;
        }
        wordJpaRepository.saveAndFlush(new Word(content));
        return true;
    }
}
