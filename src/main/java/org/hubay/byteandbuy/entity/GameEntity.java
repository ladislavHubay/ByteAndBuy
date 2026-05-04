package org.hubay.byteandbuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hubay.byteandbuy.model.game.GameState;

/**
 * Trieda reprezentuje ulozenu hru v databaze.
 * Obsahuje iba data potrebne na ulozenie.
 */
@Entity
@Table(name = "games")
@Getter
@Setter
public class GameEntity {
    /**
     * Primarny kluc pre hru. generuje sa automaticky ako UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    /**
     * Generuje sa automaticky pre zabespecenie ze sa subezne neulozi viac hier do DB.
     */
    @Version
    private Long version;
    /**
     * Aktualny stav hry (napr. PLAYING, FINISHED...) ulozene v ENUM GameState.
     */
    @Enumerated(EnumType.STRING)
    private GameState state;
    /**
     * Obsahuje kompletny stav hry (hraci, pozicie, ...).
     * Pouziva sa pre obnovu hry.
     */
    @Column(columnDefinition = "TEXT")
    private String snapshot;
}
