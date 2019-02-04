package com.gmail.onishchenko.oleksii.codenames.dto;

import com.gmail.onishchenko.oleksii.codenames.entity.Card;
import com.gmail.onishchenko.oleksii.codenames.entity.Role;

import java.util.Objects;

public class CardDto {
    private Long id;

    private String word;

    private Role role;

    private Boolean selected;
    
    private Integer cover;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getCover() {
        return cover;
    }

    public void setCover(Integer cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDto cardDto = (CardDto) o;
        return Objects.equals(id, cardDto.id) &&
                Objects.equals(word, cardDto.word) &&
                role == cardDto.role &&
                Objects.equals(selected, cardDto.selected) &&
                Objects.equals(cover, cardDto.cover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, role, selected, cover);
    }

    @Override
    public String toString() {
        return "CardDto{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", role=" + role +
                ", selected=" + selected +
                ", cover=" + cover +
                '}';
    }

    public static CardDto toDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setWord(card.getWord());
        cardDto.setRole(card.getRole());
        cardDto.setSelected(card.getSelected());
        cardDto.setCover(card.getCover());

        return cardDto;
    }
}
