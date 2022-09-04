package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.data.baseData.Item;
import de.blaumeise03.projectmanager.data.baseData.ItemRepository;
import de.blaumeise03.projectmanager.data.baseData.Player;
import de.blaumeise03.projectmanager.data.baseData.PlayerRepository;
import de.blaumeise03.projectmanager.exceptions.DataValidationException;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestmentContractService {
    @Autowired
    private InvestmentContractRepository contractRepository;

    @Autowired
    private ContractItemRepository contractItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public static List<String[]> preprocessIngameList(String ingameList) {
        List<String[]> result = new ArrayList<>();
        String[] split = ingameList.split("\n");
        for (int i = 0, splitLength = split.length; i < splitLength; i++) {
            String line = split[i];
            line = line
                    .trim()
                    .replaceAll("\t", "    ")
                    .replaceAll("^\\d+ *", "") //Delete first column (ID)
                    .replaceAll(" *[0-9.]+$", ""); //Delete last column (Valuation)

            String item = line.replaceAll(" +\\d+$", "");
            String quantity = line.replaceAll(item, "").trim();
            if (item.trim().isEmpty() || quantity.isEmpty()) {
                if (i == 0) continue;
                throw new DataValidationException("The ingame list is malformed: Missing column(s)");
            }
            result.add(new String[]{item.trim(), quantity});
        }
        return result;
    }

    public InvestmentContractPOJO findByID(long id) throws POJOMappingException {
        InvestmentContract contract = contractRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " was not found!"));
        InvestmentContractPOJO pojo = (InvestmentContractPOJO) POJOMapper.map(contract);
        pojo.getItems().sort(new ContractItemPOJO.ContractItemComparator());
        return pojo;
    }

    public List<InvestmentContractPOJO> findAll(int site, int length) {
        Pageable pageable = PageRequest.of(site, length, Sort.by("created").descending());
        Page<InvestmentContract> investmentContracts = contractRepository.findAll(pageable);
        return investmentContracts.stream().map(c -> {
            InvestmentContractPOJO contractPOJO = new InvestmentContractPOJO();
            contractPOJO.setId(c.getId());
            contractPOJO.setCreated(c.getCreated());
            contractPOJO.setPlayerID(c.getPlayer().getId());
            contractPOJO.setPlayerName(c.getPlayer().getName());
            return contractPOJO;
        }).collect(Collectors.toList());
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

    public InvestmentContractPOJO create(NewContract contract) throws POJOMappingException {
        Player player = playerRepository.findByName(contract.getPlayer()).orElseThrow(() -> new EntityNotFoundException("Player with name " + contract.getPlayer() + " not found!"));
        InvestmentContract investmentContract = new InvestmentContract();
        investmentContract.setPlayer(player);
        if (contract.getCreated() == null || contract.getCreated().equals(-1L)) {
            investmentContract.setCreated(System.currentTimeMillis() / 1000);
        } else {
            investmentContract.setCreated(contract.getCreated());
        }

        investmentContract = contractRepository.save(investmentContract);
        if (contract.getContent() == null || !contract.getContent().trim().isEmpty()) {
            List<ContractItem> items = convertItems(contract.getContent());
            InvestmentContract finalInvestmentContract = investmentContract;
            items.forEach(i -> i.setInvestmentContract(finalInvestmentContract));
            items = contractItemRepository.saveAll(items);
            investmentContract.setItems(items);
        }
        return (InvestmentContractPOJO) POJOMapper.map(investmentContract);
    }

    public List<ContractItem> convertItems(List<String[]> list) {
        List<ContractItem> contractItems = new ArrayList<>();
        for (String[] raw : list) {
            ContractItem contractItem = new ContractItem();
            Item item = itemRepository.findByItemName(raw[0]).orElseThrow(() -> new EntityNotFoundException("Item with name " + raw[0] + " was not found!"));
            contractItem.setItem(item);
            try {
                long quantity = Long.parseLong(raw[1]);
                contractItem.setQuantity(quantity);
                contractItems.add(contractItem);
            } catch (NumberFormatException e) {
                throw new DataValidationException("The quantity of item \"" + raw[0] + "\" is not a number (" + raw[1] + ")", e);
            }
        }
        return contractItems;
    }

    private List<ContractItem> convertItems(String content) {
        return convertItems(preprocessIngameList(content));
    }

    public void deleteByID(long id) {
        contractRepository.deleteById(id);
    }
}
