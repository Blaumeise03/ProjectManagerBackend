package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.PlayerRepository;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvestmentContractService {
    @Autowired
    private InvestmentContractRepository contractRepository;

    @Autowired
    private ContractItemRepository contractItemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public InvestmentContractPOJO findByID(long id) throws POJOMappingException {
        InvestmentContract contract = contractRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " was not found!"));
        InvestmentContractPOJO pojo = (InvestmentContractPOJO) POJOMapper.map(contract);
        pojo.getItems().sort(new ContractItemPOJO.ContractItemComparator());
        return pojo;
    }

    public void save(InvestmentContractPOJO contractPOJO) throws POJOMappingException {
        InvestmentContract contract;
        //Updating base data
        if (contractPOJO.getId() != null) {
            contract = contractRepository.findById(contractPOJO.getId()).orElseThrow(() -> new EntityNotFoundException("Contract with id " + contractPOJO.getId() + " was not found!"));
            POJOMapper.map(contractPOJO, contract);
        } else {
            contract = (InvestmentContract) POJOMapper.map(contractPOJO);
        }
        //Updating player
        if (!contractPOJO.getPlayerID().equals(contract.getPlayer().getId())) {
            contract.setPlayer(playerRepository.findById(contractPOJO.getPlayerID()).orElseThrow(() -> new EntityNotFoundException("Player with id " + contractPOJO.getPlayerID() + " was not found!")));
        }
        //Updating contract items
        if (contractPOJO.getId() == null) {
            //Saving contract to get the id before continuing
            contract = contractRepository.save(contract);
        }
        //Deleting old items
        List<Long> exclude = contractPOJO.getItems().stream().map(ContractItemPOJO::getId).filter(id -> id >= 0).collect(Collectors.toList());
        contractRepository.deleteItems(contract.getId(), exclude);
        //Updating items
        List<ContractItem> items = new ArrayList<>();
        for (ContractItemPOJO contractItemPOJO : contractPOJO.getItems()) {
            ContractItem contractItem = null;
            if (contractItemPOJO.getId() < 0) {
                //Creating new item
                contractItem = (ContractItem) POJOMapper.map(contractItemPOJO);
            } else {
                for (ContractItem item : contract.getItems()) {
                    if (item.getId().equals(contractItemPOJO.getId())) {
                        //Updating old item
                        POJOMapper.map(contractItemPOJO, item);
                        contractItem = item;
                        break;
                    }
                }
            }
            if (contractItem == null) {
                contractItem = contractItemRepository.findById(contractItemPOJO.getId()).orElseThrow(() -> new EntityNotFoundException("Contract item with id " + contractItemPOJO.getId() + " was not found!"));
                POJOMapper.map(contractItemPOJO, contractItem);
            }
            contractItem.setInvestmentContract(contract);
            items.add(contractItem);
        }
        if (!items.isEmpty())
            contractItemRepository.saveAll(items);
    }
}
