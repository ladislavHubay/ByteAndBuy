package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Ulozeny stav balicka kariet.
 */
@Getter
@Setter
@NoArgsConstructor
public class DeckSnapshot {
    private List<CardSnapshot> cards;
    private int currentIndex;

    public DeckSnapshot(List<CardSnapshot> cards, int currentIndex) {
        this.cards = cards;
        this.currentIndex = currentIndex;
    }
}
