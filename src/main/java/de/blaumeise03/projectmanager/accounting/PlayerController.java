package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;

    @SuppressWarnings("unchecked")
    @GetMapping("/corpTag/{tag}")
    public List<PlayerPOJO> getPlayersByCorpTag(Authentication authentication, @PathVariable String tag) throws POJOMappingException {
        return (List<PlayerPOJO>) POJOMapper.mapAll(playerService.getByCorpTag(tag));
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/corp/{id}")
    public List<PlayerPOJO> getPlayersByCorpID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return (List<PlayerPOJO>) POJOMapper.mapAll(playerService.getByCorpID(Integer.parseInt(id)));
    }

    @GetMapping("/corp/{id}/wallet")
    public List<WalletPOJO> getAllWalletsByCorpID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return playerService.findWalletsByCorpID(Integer.parseInt(id));
    }

    @GetMapping("/{id}")
    public PlayerPOJO getPlayerByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return (PlayerPOJO) POJOMapper.map(playerService.findByID(Integer.parseInt(id)));
    }

    @GetMapping("/{id}/wallet")
    public WalletPOJO getPlayerWalletByID(Authentication authentication, @PathVariable String id) throws POJOMappingException {
        return playerService.findWalletByID(Integer.parseInt(id));
    }
}
