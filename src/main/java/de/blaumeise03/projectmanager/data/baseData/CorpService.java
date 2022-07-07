package de.blaumeise03.projectmanager.data.baseData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
public class CorpService {
    @Autowired
    private CorpRepository corpRepository;

    public Corp findByID(int id) {
        return corpRepository.findById(id).orElseThrow(EntityExistsException::new);
    }
}
