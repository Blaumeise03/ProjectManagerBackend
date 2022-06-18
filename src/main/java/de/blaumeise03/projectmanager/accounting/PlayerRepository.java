package de.blaumeise03.projectmanager.accounting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByCorp(Corp corp);
}
