package org.hubay.byteandbuy.repository;

import org.hubay.byteandbuy.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository pre GameEntity.
 * Umoznuje zakladne CRUD operacie v DB.
 */
public interface GameRepository extends JpaRepository<GameEntity, String> {
}
