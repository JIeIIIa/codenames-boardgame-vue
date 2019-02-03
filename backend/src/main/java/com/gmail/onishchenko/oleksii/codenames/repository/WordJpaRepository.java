package com.gmail.onishchenko.oleksii.codenames.repository;

import com.gmail.onishchenko.oleksii.codenames.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordJpaRepository extends JpaRepository<Word, String> {
    Page<Word> findAll(Pageable pageable);

    Optional<Word> findByContent(String content);
}
