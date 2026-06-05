package org.hubay.byteandbuy.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CompanyStateDto {
    private final String name;
    private final int currentPrice;
    private final List<Integer> propertyPositions;

    public CompanyStateDto(String name, int currentPrice, List<Integer> propertyPositions) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.propertyPositions = propertyPositions;
    }
}
