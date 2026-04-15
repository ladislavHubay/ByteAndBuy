package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hubay.byteandbuy.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PropertyGroup {
    // Nazov firmy (GROUP)
    @Getter
    private final String name;
    // Zoznam policok patriacich do GROUP.
    @JsonManagedReference
    @Getter
    private final List<PropertyTile> properties;

    public PropertyGroup(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    // Na zaciatku prida kazde policko do GROUP.
    public void addProperty(PropertyTile property) {
        properties.add(property);
    }

    // Skontroluje ci hrac vlastni vsetky policka z GROUP
    public boolean ownsAll(Player player) {
        for (PropertyTile property : properties) {
            if (property.getOwner() != player) {
                return false;
            }
        }
        return true;
    }
}
