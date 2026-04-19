package org.hubay.byteandbuy.model.tiles;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hubay.byteandbuy.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje skupinu policok.
 * Ak hrac vlastni celu skupinu ziskava vyhody.
 */
public class PropertyGroup {
    @Getter
    private final String name;
    @JsonManagedReference
    @Getter
    private final List<PropertyTile> properties;

    public PropertyGroup(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    /**
     * Prida policko do skupiny. Aplikuje sa pri vytvarani hry.
     */
    public void addProperty(PropertyTile property) {
        properties.add(property);
    }

    /**
     * Skontroluje ci hrac vlastni vsetky policka zo skupiny.
     * Pouziva sa na urcenie popaltku na policku.
     * @param player Hrac ktoreho vlastnictvo sa kontroluje.
     * @return true ak vlastni vsetky policka zo skupiny, false ak nie.
     */
    public boolean ownsAll(Player player) {
        for (PropertyTile property : properties) {
            if (property.getOwner() != player) {
                return false;
            }
        }
        return true;
    }
}
