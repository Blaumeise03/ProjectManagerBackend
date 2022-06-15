package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.exceptions.EntityNotFoundException;
import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    public Transaction findById(int id){
        return transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public long getSumByPlayer(User user, int playerID) throws MissingPermissionsException {
        Player player = playerRepository.getById(playerID);
        if(hasAccessToWallet(user, playerID)) {
            return transactionRepository.getWalletSumOfPlayer(playerID);
        } else {
            throw new MissingPermissionsException(String.format("The user %d (%s) does not has read-access to wallet of player %d (%s)", user.getId(), user.getUsername(), playerID, player.getName()));
        }
    }

    public List<Transaction> findAllByUserID(User user, int playerID) throws MissingPermissionsException {
        Player player = playerRepository.getById(playerID);
        boolean hasAccess = hasAccessToWallet(user, playerID);
        if(hasAccess)
            return transactionRepository.findByFromOrToOrderByTimeDesc(player, player);
        throw new MissingPermissionsException(String.format("The user %d (%s) does not has read-access to wallet of player %d (%s)", user.getId(), user.getUsername(), playerID, player.getName()));
    }

    public Transaction save(User user, TransactionPOJO transactionPOJO) throws MissingPermissionsException, POJOMappingException {
        //Player walletOwner = transactionPOJO.fromID != -1 ? playerRepository.getById(transactionPOJO.fromID) : null;
        boolean isAdmin = user.hasAdminPerms(-1);
        if(user.ownsWallet(transactionPOJO.fromID) || isAdmin) {
            if(transactionPOJO.isVerified() && !isAdmin)
                throw new MissingPermissionsException("Missing permissions to verify transactions!");
            Transaction transaction;
            try {
                transaction = transactionRepository.getById(transactionPOJO.tid);
                if(transaction.isVerified() && !isAdmin)
                    throw new MissingPermissionsException("Missing permissions to edit verified transactions!");
            }catch (javax.persistence.EntityNotFoundException ignored){}

            transaction = (Transaction) POJOMapper.map(transactionPOJO);
            transaction.setFrom(transactionPOJO.getFromID() != -1 ? playerRepository.getById(transactionPOJO.getFromID()) : null);
            transaction.setTo(transactionPOJO.getToID() != -1 ? playerRepository.getById(transactionPOJO.getToID()) : null);
            if(transaction.getFrom() == null && transaction.getTo() == null)
                throw new UnsupportedOperationException("The transaction need to have at least one player!");
            return transactionRepository.save(transaction);
        } else {
            throw new MissingPermissionsException(String.format("The user %d (%s) does not has access to wallet %d", user.getId(), user.getUsername(), transactionPOJO.getFromID()));
        }
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public void deleteById(int id){
        transactionRepository.deleteById(id);
    }

    public boolean hasAuthorization() {
        return true;
    }

    public boolean hasAccessToWallet(User user, int playerID) {
        Player player = playerRepository.getById(playerID);
        boolean hasAccess = user.hasAdminPerms(-1);
        if(!hasAccess) {
            for (Player p : user.getPlayers()) {
                if (p.getCorp() != null && player.getCorp() != null && p.getCorp().equals(player.getCorp())) {
                    hasAccess = true;
                    break;
                }
            }
        }
        return hasAccess;
    }
}
