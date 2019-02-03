package com.gmail.onishchenko.oleksii.codenames.entity;

public class CardBuilder {
    private Long id;

    private String word;

    private Role role;

    private Boolean selected = false;

    private Integer cover;

    private Room room;

    private CardBuilder() {
    }
    
    public static CardBuilder getInstance() {
        return new CardBuilder();
    }

    public CardBuilder id(Long id) {
        this.id = id;

        return this;
    }

    public CardBuilder word(String word) {
        this.word = word;

        return this;
    }

    public CardBuilder role(Role role) {
        this.role = role;

        return this;
    }

    public CardBuilder selected(Boolean selected) {
        this.selected = selected;

        return this;
    }

    public CardBuilder cover(Integer cover) {
        this.cover = cover;

        return this;
    }

    public CardBuilder room(Room room) {
        this.room = room;

        return this;
    }

    public Card build(){
        Card card = new Card();
        card.setId(id);
        card.setWord(word);
        card.setRole(role);
        card.setSelected(selected);
        card.setCover(cover);
        card.setRoom(room);

        return card;
    }
}
