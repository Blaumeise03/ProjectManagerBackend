package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i.itemName from Item i ORDER BY i.order, i.itemName")
    List<String> findAllItemNames();

    Optional<Item> findByItemName(String name);
}
