package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}/blueprint")
    public BlueprintPOJO getItemBPByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return itemService.findItemBpByID(Long.valueOf(id));
    }

    @PostMapping
    public ItemPOJO saveItem(@RequestBody ItemPOJO itemPOJO) throws POJOMappingException {
        Item item = itemService.save(itemPOJO);
        ItemPOJO res = (ItemPOJO) POJOMapper.map(item);
        if(item.getBlueprint() != null) res.setBlueprint((BlueprintPOJO) POJOMapper.map(item.getBlueprint()));
        return res;
    }
}
