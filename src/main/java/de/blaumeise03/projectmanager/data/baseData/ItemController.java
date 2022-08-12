package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/full")
    public List<ItemPOJO> getAllItems(Authentication authentication) throws POJOMappingException {
        return itemService.findFullAll();
    }

    @GetMapping("/allNames")
    public List<String> getAllItemNames(Authentication authentication) {
        return itemService.findAllItemNames();
    }

    @GetMapping("/{id}")
    public ItemPOJO getItemByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return (ItemPOJO) POJOMapper.map(itemService.findItemByID(Long.valueOf(id)));
    }

    @DeleteMapping("/{id}")
    public boolean deleteItemByID(Authentication authentication, @PathVariable String id) {
        return itemService.delete(Long.parseLong(id));
    }

    @GetMapping("/{id}/full")
    public ItemPOJO getFullItemByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return itemService.findFullItemByID(Long.valueOf(id));
    }

    @GetMapping("/byName/{name}")
    public ItemPOJO getItemByName(Authentication authentication, @PathVariable String name) throws POJOMappingException {
        return itemService.findByName(name);
    }

    @GetMapping("/{id}/price")
    public List<PricePOJO> getItemPricesByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return itemService.findItemPricesByID(Long.valueOf(id));
    }

    @PostMapping("/prices")
    public Map<Long, List<PricePOJO>> getBatchPrices(Authentication authentication, @RequestBody List<Long> ids) throws POJOMappingException {
        return itemService.findBatchPrices(ids);
    }

    @GetMapping("/{id}/blueprint")
    public BlueprintPOJO getItemBPByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return itemService.findItemBpByID(Long.valueOf(id));
    }

    @PostMapping
    public ItemPOJO saveItem(@RequestBody ItemPOJO itemPOJO) throws POJOMappingException {
        return  itemService.save(itemPOJO);
    }
}
