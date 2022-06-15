package de.blaumeise03.projectmanager.accounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByFromOrToOrderByTimeDesc(Player from, Player to);

    @Query(value = "SELECT (SELECT SUM(price) priceSum FROM db_transactions WHERE idto=?1 AND verified=1) - (SELECT SUM(price) priceSum FROM db_transactions WHERE idfrom=?1 AND verified=1) AS balance", nativeQuery = true)
    long getWalletSumOfPlayer(int playerId);
}
