package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private BlueprintRepository blueprintRepository;

    public Item findItemByID(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));
    }

    public ItemPOJO findFullItemByID(Long id) throws POJOMappingException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));
        return mapFullItem(item);
    }

    public List<ItemPOJO> findFullAll() throws POJOMappingException {
        List<Item> items = itemRepository.findAll();
        return mapFullItems(items);
    }

    public ItemPOJO findByName(String name) throws POJOMappingException {
        return (ItemPOJO) POJOMapper.map(itemRepository.findByItemName(name).orElseThrow(() -> new EntityNotFoundException("Item with name " + name + " not found!")));
    }

    @SuppressWarnings("unchecked")
    public List<PricePOJO> findItemPricesByID(Long id) throws POJOMappingException {
        return (List<PricePOJO>) POJOMapper.mapAll(priceRepository.findPricesByItemID(id).orElseThrow(() -> new EntityNotFoundException("Item price from item-id " + id + " not found")));
    }

    public BlueprintPOJO findItemBpByID(Long id) throws POJOMappingException {
        return (BlueprintPOJO) POJOMapper.map(blueprintRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BP with id " + id + " not found")));
    }

    public List<String> findAllItemNames() {
        return itemRepository.findAllItemNames();
    }

    public ItemPOJO save(ItemPOJO itemPOJO) throws POJOMappingException {
        if(itemPOJO.getItemID() != null) {
            //Updating old item
            Optional<Item> optionalItem = itemRepository.findById(itemPOJO.getItemID());
            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();
                item.setItemName(itemPOJO.getItemName());
                item.setItemType(Item.ItemType.valueOf(itemPOJO.getItemType()));
                if(itemPOJO.getBlueprint() == null && item.getBlueprint() != null) {
                    //Blueprint was removed, deleting it...
                    blueprintRepository.delete(item.getBlueprint());
                    item.setBlueprint(null);
                } else {
                    saveBlueprint(item, itemPOJO.getBlueprint());
                }
                if(itemPOJO.getPrices() != null)
                    savePrices(item, itemPOJO.getPrices());
                return mapFullItem(itemRepository.save(item));
            }
            throw new IllegalArgumentException("Item has an ID but is not registered in the database! Manual ID assignment is not possible!");
        }
        //Creating new item
        Item newItem = (Item) POJOMapper.map(itemPOJO);
        //Saves item first to get the ID required for the blueprint
        newItem = itemRepository.save(newItem);
        if(itemPOJO.getBlueprint() != null) {
            itemPOJO.getBlueprint().setItem(newItem.getItemID());
            saveBlueprint(newItem, itemPOJO.getBlueprint());
            itemRepository.save(newItem);
        }
        if(itemPOJO.getPrices() != null) savePrices(newItem, itemPOJO.getPrices());
        Item i = itemRepository.save(newItem);
        return mapFullItem(i);
    }

    public boolean delete(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()) {
            itemRepository.delete(item.get());
            return true;
        }
        return false;
    }

    List<ItemPOJO> mapFullItems(List<Item> items) throws POJOMappingException {
        List<ItemPOJO> res = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemPOJO r = mapFullItem(item);
            res.add(r);
        }
        return res;
    }

    ItemPOJO mapFullItem(Item item) throws POJOMappingException {
        ItemPOJO res = (ItemPOJO) POJOMapper.map(item);
        mapFullItem(res, item);
        return res;
    }

    void mapFullItem(ItemPOJO res, Item item) throws POJOMappingException {
        if(item.getBlueprint() != null)
            res.setBlueprint((BlueprintPOJO) POJOMapper.map(item.getBlueprint()));
        if(item.getPrices() != null)
            //noinspection unchecked
            res.setPrices((Collection<PricePOJO>) POJOMapper.mapAll(item.getPrices()));
    }

    public Blueprint saveBlueprint(Item item, BlueprintPOJO blueprintPOJO) throws POJOMappingException {
        if (blueprintPOJO != null) {
            if (blueprintPOJO.getItem() == null)
                blueprintPOJO.setItem(item.getItemID());
            if (item.getBlueprint() != null) {
                List<Long> exclude = new ArrayList<>(item.getBlueprint().getBaseCost().size());
                for(ItemCostPOJO itemCost : blueprintPOJO.getBaseCost()) exclude.add(itemCost.getItem());
                blueprintRepository.deleteItemCost(item.getItemID(), exclude);
                POJOMapper.map(blueprintPOJO, item.getBlueprint());
                for (ItemCostPOJO costPOJO : blueprintPOJO.getBaseCost()) {
                    boolean found = false;
                    for (ItemCost cost : item.getBlueprint().getBaseCost()) {
                        if (cost.getItem().getItemID().equals(costPOJO.getItem())) {
                            cost.setQuantity(costPOJO.getQuantity());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        item.getBlueprint().getBaseCost().add((ItemCost) POJOMapper.map(costPOJO));
                    }
                }
            } else {
                Blueprint bp = (Blueprint) POJOMapper.map(blueprintPOJO);
                //noinspection unchecked
                bp.setBaseCost(new HashSet<>((Collection<? extends ItemCost>) POJOMapper.mapAll(blueprintPOJO.getBaseCost())));
                item.setBlueprint(bp);
            }
            item.getBlueprint().getBaseCost().forEach(b -> b.setBlueprint(item.getBlueprint()));
        }
        return item.getBlueprint();
    }

    public void savePrices(Item item, Collection<PricePOJO> pricePOJOS) throws POJOMappingException {
        Collection<Price> prices = null;
        if (item.getPrices() != null) {
            prices = item.getPrices();
        } else {
            prices = new ArrayList<>(pricePOJOS.size());
        }
        List<String> exclude = new ArrayList<>();
        pricePOJOS.forEach(p -> exclude.add(p.getPriceType()));
        priceRepository.deletePrices(item.getItemID(), exclude);
        for (PricePOJO pricePOJO : pricePOJOS) {
            boolean found = false;
            for (Price price : prices) {
                if (price.getPriceType().equals(pricePOJO.getPriceType())) {
                    found = true;
                    POJOMapper.map(pricePOJO, price);
                }
            }
            if (!found) {
                prices.add((Price) POJOMapper.map(pricePOJO));
            }
        }
        prices.forEach(p -> p.setItem(item));
    }
}
