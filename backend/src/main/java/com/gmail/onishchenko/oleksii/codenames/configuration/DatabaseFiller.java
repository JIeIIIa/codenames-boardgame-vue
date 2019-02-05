package com.gmail.onishchenko.oleksii.codenames.configuration;

import com.gmail.onishchenko.oleksii.codenames.service.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class DatabaseFiller implements CommandLineRunner {

    private static final Logger log = LogManager.getLogger(DatabaseFiller.class);
    private static final String WORDS_FILENAME = "/words.txt";

    private final WordService wordService;

    @Autowired
    public DatabaseFiller(WordService wordService) {
        log.info("Create instance of {}", DatabaseFiller.class.getName());
        this.wordService = wordService;
    }


    @Override
    public void run(String... args) throws Exception {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger wordsRead = new AtomicInteger(0);
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(getClass().getResourceAsStream(WORDS_FILENAME))
                     )) {
            reader.lines().forEach(w -> {
                wordsRead.incrementAndGet();
                if (wordService.insert(w)) {
                    count.incrementAndGet();
                }
            });
        }
        log.info("{} words were read from '{}'", wordsRead.get(), WORDS_FILENAME);
        log.info("{} words were added in database", count.get());
    }
}
