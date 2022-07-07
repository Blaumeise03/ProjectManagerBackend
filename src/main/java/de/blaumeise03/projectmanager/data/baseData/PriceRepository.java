package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Price.PriceID> {
    @Query(value = "SELECT * FROM db_prices WHERE itemid = ?1", nativeQuery = true)
    Optional<List<Price>> findPricesByItemID(long itemID);
}
