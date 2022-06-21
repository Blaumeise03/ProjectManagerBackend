package de.blaumeise03.projectmanager.accounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByCorp(Corp corp);

    Optional<Player> findByName(String name);

    /**

     */
    @Query(nativeQuery = true, value = "SELECT (SELECT name FROM db_players WHERE uid=?1) AS name, (SELECT (SELECT SUM(price) priceSum FROM db_transactions WHERE idto=?1 AND verified=?1) - (SELECT SUM(price) priceSum FROM db_transactions WHERE idfrom=?1 AND verified=?1) )AS verified, (SELECT (SELECT SUM(price) priceSum FROM db_transactions WHERE idto=?1 AND verified=0) - (SELECT SUM(price) priceSum FROM db_transactions WHERE idfrom=?1 AND verified=0) )AS unverified")
    Optional<Tuple> findWalletByID(Integer id);
}
