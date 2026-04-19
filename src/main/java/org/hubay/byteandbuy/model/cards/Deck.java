package org.hubay.byteandbuy.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reprezentuje balicek kariet.
 */
public class Deck {
    private final List<Card> cards;
    private int currentIndex = 0;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        shuffle();
    }

    /**
     * Potiahne kartu. Po vystriedani vsetkych kariet sa balicek zamiesa
     * a taha sa znovu z pomiesaneho balicka.
     */
    public Card draw() {
        if (currentIndex >= cards.size()) {
            shuffle();
            currentIndex = 0;
        }

        return cards.get(currentIndex++);
    }

    /**
     * Premiesa poradie kariet.
     */
    private void shuffle() {
        Collections.shuffle(cards);
    }
}
