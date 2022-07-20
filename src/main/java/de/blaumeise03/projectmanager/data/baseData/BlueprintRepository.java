package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {

    @Query(nativeQuery = true, value = "DELETE FROM db_bp_item_cost WHERE blueprintid = ?1 AND itemid NOT IN ?2")
    void deleteItemCost(long blueprintID, List<Long> exclude);
}
