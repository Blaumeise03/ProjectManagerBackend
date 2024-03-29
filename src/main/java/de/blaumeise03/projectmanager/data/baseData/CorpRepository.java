package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorpRepository extends JpaRepository<Corp, Integer> {
    Optional<Corp> findByTag(String tag);
}
