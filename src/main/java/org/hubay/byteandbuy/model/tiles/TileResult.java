package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;

@Getter
public class TileResult {
    private final String message;
    private final boolean requiresDecision;

    public TileResult(String message, boolean requiresDecision) {
        this.message = message;
        this.requiresDecision = requiresDecision;
    }

    public static TileResult simple(String message) {
        return new TileResult(message, false);
    }

    public static TileResult decision(String message) {
        return new TileResult(message, true);
    }
}
