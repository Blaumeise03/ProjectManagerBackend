package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.userManagement.UserSessionInfoPOJO;
import de.blaumeise03.projectmanager.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CorpRepository corpRepository;

    @Autowired
    UserService userService;

    public List<Player> getByCorpTag(String corpTag) {
        Optional<Corp> corp = corpRepository.findByTag(corpTag);
        if(corp.isEmpty()) throw new EntityNotFoundException("Corp with tag \"" + corpTag + "\" was not found in the database!");
        return playerRepository.findByCorp(corp.get());
    }

    public List<Player> getByCorpID(int id) {
        Optional<Corp> corp = corpRepository.findById(id);
        if(corp.isEmpty()) throw new EntityNotFoundException("Corp with id " + id + " was not found in the database!");
        return playerRepository.findByCorp(corp.get());
    }

    public Player findByID(int id) {
        return playerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public WalletPOJO findWalletByID(int id) {
        Tuple tuple = playerRepository.findWalletByID(id).orElseThrow(EntityNotFoundException::new);
        return new WalletPOJO(id, tuple.get("name", String.class), tuple.get("verified", BigDecimal.class), tuple.get("unverified", BigDecimal.class));
    }

    public List<WalletPOJO> findWalletsByCorpID(int id) {
        Corp corp = corpRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Player> players = playerRepository.findByCorp(corp);
        List<WalletPOJO> res = new ArrayList<>();
        for(Player p : players) {
            Tuple tuple = playerRepository.findWalletByID(p.getUid()).orElseThrow(EntityNotFoundException::new);
            res.add(new WalletPOJO(p.getUid(), tuple.get("name", String.class), tuple.get("verified", BigDecimal.class), tuple.get("unverified", BigDecimal.class)));
        }
        return res;
    }
}
