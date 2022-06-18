package de.blaumeise03.projectmanager.accounting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByCorp(Corp corp);

    Optional<Player> findByName(String name);
}
