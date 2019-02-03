package com.gmail.onishchenko.oleksii.codenames.service;

import com.gmail.onishchenko.oleksii.codenames.entity.Word;

import java.util.Set;

public interface WordService {

    Set<Word> randomWords(int size);

    boolean insert(String content);
}
