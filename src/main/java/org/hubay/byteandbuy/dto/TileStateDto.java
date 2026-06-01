package org.hubay.byteandbuy.dto;

import lombok.Getter;

@Getter
public class TileStateDto {
    private final int position;
    private final String name;
    private final String type;
    private final boolean buyable;
    private final Integer price;
    private final PlayerSummary owner;

    public TileStateDto(int position, String name, String type, boolean buyable,
                        Integer price, PlayerSummary owner) {
        this.position = position;
        this.name = name;
        this.type = type;
        this.buyable = buyable;
        this.price = price;
        this.owner = owner;
    }
}
