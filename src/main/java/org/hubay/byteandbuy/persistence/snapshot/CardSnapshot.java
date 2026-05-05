package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hubay.byteandbuy.model.cards.CardKind;

import java.util.UUID;

/**
 * Ulozeny stav jednej konkretnej karty v balicku.
 */
@Getter
@Setter
@NoArgsConstructor
public class CardSnapshot {
    private UUID id;
    private CardKind kind;
    private String description;
    private Integer amount;
    private Integer steps;
    private Integer position;
    private Boolean applyBonusStart;

    public CardSnapshot(UUID id, CardKind kind, String description) {
        this.id = id;
        this.kind = kind;
        this.description = description;
    }
}
