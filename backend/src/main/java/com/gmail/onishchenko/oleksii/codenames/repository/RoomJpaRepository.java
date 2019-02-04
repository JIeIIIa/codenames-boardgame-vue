package com.gmail.onishchenko.oleksii.codenames.repository;

import com.gmail.onishchenko.oleksii.codenames.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomJpaRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByTitle(String title);
}
