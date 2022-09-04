package de.blaumeise03.projectmanager.data.projects.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestmentContractRepository extends JpaRepository<InvestmentContract, Long> {
    @Query(nativeQuery = true, value = "DELETE FROM db_contract_items WHERE contractID = ?1 AND id NOT IN ?2")
    void deleteItems(Long contractID, List<Long> exclude);


}
