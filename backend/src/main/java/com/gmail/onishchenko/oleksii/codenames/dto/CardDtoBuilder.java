package com.gmail.onishchenko.oleksii.codenames.dto;

import com.gmail.onishchenko.oleksii.codenames.entity.Role;

public class CardDtoBuilder {
    private Long id;

    private String word;

    private Role role;

    private Boolean selected;

    private Integer cover;

    private CardDtoBuilder() {
    }

    public static CardDtoBuilder getInstance() {
        return new CardDtoBuilder();
    }

    public CardDtoBuilder id(Long id) {
        this.id = id;

        return this;
    }

    public CardDtoBuilder word(String word) {
        this.word = word;

        return this;
    }

    public CardDtoBuilder role(Role role) {
        this.role = role;

        return this;
    }

    public CardDtoBuilder selected(Boolean selected) {
        this.selected = selected;

        return this;
    }

    public CardDtoBuilder cover(Integer cover) {
        this.cover = cover;

        return this;
    }

    public CardDto build() {
        CardDto cardDto = new CardDto();
        cardDto.setCover(cover);
        cardDto.setSelected(selected);
        cardDto.setRole(role);
        cardDto.setWord(word);
        cardDto.setId(id);

        return cardDto;
    }
}
