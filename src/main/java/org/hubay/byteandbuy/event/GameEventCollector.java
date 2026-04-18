package org.hubay.byteandbuy.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameEventCollector {
    private final List<String> events = new ArrayList<>();

    public void add(String event) {
        events.add(event);
    }

}
