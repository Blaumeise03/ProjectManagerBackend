package de.blaumeise03.projectmanager.data.projects.contract;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project/contract")
public class InvestmentContractController {
    @Autowired
    private InvestmentContractService contractService;

    @GetMapping("/all")
    public List<InvestmentContractPOJO> getAllContracts(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "50") String length) {
        return contractService.findAll(Integer.parseInt(page), Integer.parseInt(length));
    }

    @PostMapping("/new")
    public InvestmentContractPOJO createContract(@RequestBody NewContract contract) throws POJOMappingException {
        return contractService.create(contract);
    }

    @GetMapping("/{id}")
    public InvestmentContractPOJO getContractByID(@PathVariable String id) throws POJOMappingException {
        return contractService.findByID(Long.parseLong(id));
    }

    @DeleteMapping("/{id}")
    public void deleteContractByID(@PathVariable String id) throws POJOMappingException {
        contractService.deleteByID(Long.parseLong(id));
    }

    @PostMapping
    public void saveContract(@RequestBody InvestmentContractPOJO contractPOJO) throws POJOMappingException {
        contractService.save(contractPOJO);
    }
}
