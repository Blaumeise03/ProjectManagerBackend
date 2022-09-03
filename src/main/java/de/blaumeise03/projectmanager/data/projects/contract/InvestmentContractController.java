package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project/contract")
public class InvestmentContractController {
    @Autowired
    private InvestmentContractService contractService;

    @GetMapping("/{id}")
    public InvestmentContractPOJO getContractByID(@PathVariable String id) throws POJOMappingException {
        return contractService.findByID(Long.parseLong(id));
    }

    @PostMapping
    public void saveContract(@RequestBody InvestmentContractPOJO contractPOJO) throws POJOMappingException {
        contractService.save(contractPOJO);
    }
}
