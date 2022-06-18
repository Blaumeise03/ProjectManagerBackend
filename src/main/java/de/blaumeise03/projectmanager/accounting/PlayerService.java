package de.blaumeise03.projectmanager.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CorpRepository corpRepository;

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
}
