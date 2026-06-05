package org.hubay.byteandbuy.model.tiles;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje zoznam vsetkych firiem.
 */
public class Companies {
    @Getter
    private final List<PropertyGroup> groups;

    public Companies() {
        this.groups = new ArrayList<>();
    }

    /**
     * Prida firmu do zoznamu firiem.
     */
    public void addGroup(PropertyGroup propertyGroup) {
        groups.add(propertyGroup);
    }
}
