package org.hubay.byteandbuy.service;

import org.hubay.byteandbuy.entity.GameEntity;
import org.hubay.byteandbuy.repository.GameRepository;
import org.springframework.stereotype.Service;

/**
 * Poskytuje jednoduche API pre ukladanie a nacitanie GameEntity.
 */
@Service
public class GamePersistenceService {
    private final GameRepository repository;

    public GamePersistenceService(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Uklada alebo aktualizuje GameEntity v DB.
     */
    public GameEntity save(GameEntity entity) {
        return repository.save(entity);
    }

    /**
     * Najde a nacita hru podla ID.
     */
    public GameEntity get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }
}
