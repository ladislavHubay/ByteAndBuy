package org.hubay.byteandbuy.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardStateDto {
    private final List<TileStateDto> tiles;

    public BoardStateDto(List<TileStateDto> tiles) {
        this.tiles = tiles;
    }
}
