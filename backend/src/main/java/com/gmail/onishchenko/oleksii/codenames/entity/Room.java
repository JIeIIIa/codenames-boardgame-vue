package com.gmail.onishchenko.oleksii.codenames.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Room implements Serializable {

    private static final long serialVersionUID = 5614823692254921638L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30)
    private String title;

    @Size(max = 30)
    private String password;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    private LocalDateTime dateModified;

    public Room() {
    }

    public Room(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public void addCard(Card card) {
        cards.add(card);
        card.setRoom(this);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        card.setRoom(null);
    }

    public void removeAllCards() {
        for (int i = cards.size() - 1; i >= 0; i--) {
            removeCard(cards.get(i));
        }
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(title, room.title) &&
                Objects.equals(password, room.password) &&
                Objects.equals(cards, room.cards) &&
                Objects.equals(dateModified, room.dateModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, password, cards, dateModified);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cards=" + cards +
                ", dateModified=" + dateModified +
                '}';
    }
}
