package org.hubay.byteandbuy.persistence.snapshot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje aktualnu trhovu cenu firmy.
 */
@Getter
@Setter
@NoArgsConstructor
public class CompanySnapshot {
    private String companyName;
    private int currentPrice;
    private int initialPrice;

    public CompanySnapshot(String companyName, int currentPrice, int initialPrice) {
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.initialPrice = initialPrice;
    }
}
