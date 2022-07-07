package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/corp")
public class CorpController {
    @Autowired
    private CorpService corpService;

    @GetMapping("{id}")
    public CorpPOJO findByID(@PathVariable String id) throws POJOMappingException {
        return (CorpPOJO) POJOMapper.map(corpService.findByID(Integer.parseInt(id)));
    }
}
