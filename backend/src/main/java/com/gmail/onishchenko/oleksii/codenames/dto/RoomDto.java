package com.gmail.onishchenko.oleksii.codenames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.onishchenko.oleksii.codenames.entity.Room;

import javax.validation.constraints.Size;
import java.util.Objects;

public class RoomDto {

    private Long id;

    @Size(max = 30)
    private String title;

    @Size(max = 30)
    @JsonIgnore
    private String password;

    public RoomDto() {
    }

    public RoomDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return Objects.equals(id, roomDto.id) &&
                Objects.equals(title, roomDto.title) &&
                Objects.equals(password, roomDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, password);
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public static RoomDto toDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setTitle(room.getTitle());
        roomDto.setPassword(null);

        return roomDto;
    }
}
