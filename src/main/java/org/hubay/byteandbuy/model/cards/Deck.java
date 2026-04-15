package org.hubay.byteandbuy.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Balicek kariet
public class Deck {
    // Balicek - zoznam obsahujuca karty.
    private final List<Card> cards;
    // Index karty v balicku - zozname.
    private int currentIndex = 0;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        shuffle();
    }

    // Metoda sa stara o balicek kariet a vrati potiahnutu kartu.
    // (taha karty v poradi, pri potiahnuti vsetkych kariet zamiesa balicek)
    public Card draw() {
        if (currentIndex >= cards.size()) {
            shuffle();
            currentIndex = 0;
        }

        return cards.get(currentIndex++);
    }

    // zamiesa karty (zamiesa poradie prvkov v zozname)
    private void shuffle() {
        Collections.shuffle(cards);
    }
}
