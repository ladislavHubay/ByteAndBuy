package model.cards;

import java.util.*;

// Balicek kariet
public class Deck {
    private final List<Card> cards;
    private int currentIndex = 0;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        shuffle();
    }

    public Card draw() {
        if (currentIndex >= cards.size()) {
            shuffle();
            currentIndex = 0;
        }

        return cards.get(currentIndex++);
    }

    private void shuffle() {
        Collections.shuffle(cards);     // zamiesa karty (zamiesa poradie prvkov v zozname)
    }
}
