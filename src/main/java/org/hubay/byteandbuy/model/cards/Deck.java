package org.hubay.byteandbuy.model.cards;

import org.hubay.byteandbuy.persistence.snapshot.CardSnapshot;
import org.hubay.byteandbuy.persistence.snapshot.DeckSnapshot;

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

    public Deck(List<Card> cards, int currentIndex) {
        this.cards = new ArrayList<>(cards);
        this.currentIndex = currentIndex;
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
     * Vytvori snapshot balicka vratane aktualneho poradia kariet.
     */
    public DeckSnapshot toSnapshot() {
        List<CardSnapshot> cardSnapshots = new ArrayList<>();

        for (Card card : cards) {
            cardSnapshots.add(card.toSnapshot());
        }

        return new DeckSnapshot(cardSnapshots, currentIndex);
    }

    /**
     * Premiesa poradie kariet.
     */
    private void shuffle() {
        Collections.shuffle(cards);
    }
}
