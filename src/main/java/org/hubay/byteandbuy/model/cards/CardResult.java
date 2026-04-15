package org.hubay.byteandbuy.model.cards;

import lombok.Getter;

@Getter
public class CardResult {
    private final String message;
    private final Integer moveSteps;

    private CardResult(String message, Integer moveSteps) {
        this.message = message;
        this.moveSteps = moveSteps;
    }

    public static CardResult simple(String message) {
        return new CardResult(message, null);
    }
}
