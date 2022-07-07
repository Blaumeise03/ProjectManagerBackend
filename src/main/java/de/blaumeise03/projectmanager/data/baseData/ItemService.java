package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOConverter;
import de.blaumeise03.projectmanager.utils.POJOMapper;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
        ItemPOJO res = (ItemPOJO) POJOMapper.map(item);
        if(item.getBlueprint() != null)
            res.setBlueprint((BlueprintPOJO) POJOMapper.map(item.getBlueprint()));
        if(item.getPrices() != null)
            //noinspection unchecked
            res.setPrices((Collection<PricePOJO>) POJOMapper.mapAll(item.getPrices()));
        return res;
    }

    public List<ItemPOJO> findFullAll() throws POJOMappingException {
        List<Item> items = itemRepository.findAll();
        List<ItemPOJO> res = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemPOJO r = (ItemPOJO) POJOMapper.map(item);
            if(item.getBlueprint() != null)
                r.setBlueprint((BlueprintPOJO) POJOMapper.map(item.getBlueprint()));
            if(item.getPrices() != null)
                //noinspection unchecked
                r.setPrices((Collection<PricePOJO>) POJOMapper.mapAll(item.getPrices()));
            res.add(r);
        }
        return res;
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

    public Item softSave(ItemPOJO itemPOJO) throws POJOMappingException {
        if(itemPOJO.getItemID() != null) {
            Optional<Item> optionalItem = itemRepository.findById(itemPOJO.getItemID());
            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();
                item.setItemName(itemPOJO.getItemName());
                item.setItemType(Item.ItemType.valueOf(itemPOJO.getItemType()));
                if (itemPOJO.getBlueprint() != null) {
                    if (itemPOJO.getBlueprint().getItem() == null)
                        itemPOJO.getBlueprint().setItem(itemPOJO.getItemID());
                    if(item.getBlueprint() != null) {

                    }
                    Blueprint bp = (Blueprint) POJOMapper.map(itemPOJO.getBlueprint());
                    //bp.setId(itemPOJO.getItemID());
                    bp.getBaseCost().forEach(b -> b.setBlueprint(bp));
                    item.setBlueprint(bp);
                }
                return itemRepository.save(item);
            }
        }
        Item newItem = (Item) POJOMapper.map(itemPOJO);
        return itemRepository.save(newItem);
    }
}
