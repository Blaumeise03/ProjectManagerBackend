package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.UserService;
import de.blaumeise03.projectmanager.utils.AuthenticationUtils;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public TransactionPOJO getTransaction(Authentication authentication, @PathVariable String id) {
        //System.out.println("Test " + id);
        return TransactionPOJO.convert(transactionService.findById(Integer.parseInt(id)));
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

    @PostMapping("/new")
    public ResponseEntity<String> putTransaction(Authentication authentication, @RequestBody TransactionPOJO transactionPOJO) throws MissingPermissionsException, POJOMappingException {
        //System.out.println(transactionPOJO);

        transactionService.save(AuthenticationUtils.getUser(authentication, userService), transactionPOJO);
        return ResponseEntity.ok().body("Transaction saved!");
    }
}
