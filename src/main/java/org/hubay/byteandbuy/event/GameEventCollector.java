package org.hubay.byteandbuy.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Sluzi na ukladanie logov.
 */
@Getter
public class GameEventCollector {
    private final List<String> events = new ArrayList<>();

    public void add(String event) {
        events.add(event);
    }
}
