package de.blaumeise03.projectmanager.data.accounting;

import de.blaumeise03.projectmanager.data.baseData.PlayerService;
import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.utils.AuthenticationUtils;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/bank/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/{id}")
    public TransactionPOJO getTransaction(Authentication authentication, @PathVariable String id) throws MissingPermissionsException, POJOMappingException {
        return (TransactionPOJO) POJOMapper.map(transactionService.findById(AuthenticationUtils.getUser(authentication, userService), Integer.parseInt(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(Authentication authentication, @PathVariable String id) throws EntityNotFoundException, MissingPermissionsException {
        transactionService.delete(AuthenticationUtils.getUser(authentication, userService), Integer.parseInt(id));
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/user/{id}")
    public List<TransactionPOJO> getTransactionsByUser(Authentication authentication, @PathVariable String id) throws POJOMappingException, MissingPermissionsException {
        return (List<TransactionPOJO>) POJOMapper.mapAll(transactionService.findAllByUserID(AuthenticationUtils.getUser(authentication, userService), Integer.parseInt(id)));
    }

    @GetMapping("/user/{id}/sum")
    public ResponseEntity<Long> getWalletSumByUser(Authentication authentication, @PathVariable String id) throws MissingPermissionsException {
        long res = transactionService.getSumByPlayer(AuthenticationUtils.getUser(authentication, userService), Integer.parseInt(id));
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("")
    public ResponseEntity<String> putTransaction(Authentication authentication, @RequestBody TransactionPOJO transactionPOJO) throws MissingPermissionsException, POJOMappingException {
        //System.out.println(transactionPOJO);

        transactionService.save(AuthenticationUtils.getUser(authentication, userService), transactionPOJO);
        return ResponseEntity.ok().body("Transaction saved!");
    }
}
