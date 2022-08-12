package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Price.PriceID> {
    @Query(value = "SELECT * FROM db_prices WHERE itemid = ?1", nativeQuery = true)
    Optional<List<Price>> findPricesByItemID(long itemID);

    @Query(value = "SELECT * FROM db_prices WHERE itemid in ?1", nativeQuery = true)
    Optional<List<Price>> findPricesByItemIDs(List<Long> itemIDs);

    @Query(nativeQuery = true, value = "DELETE FROM db_prices WHERE itemid = ?1 AND price_type NOT IN ?2")
    void deletePrices(long itemID, List<String> exclude);
}
