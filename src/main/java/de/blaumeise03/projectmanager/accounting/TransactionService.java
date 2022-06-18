package de.blaumeise03.projectmanager.accounting;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.utils.POJOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    public Transaction findById(User user, int id) throws MissingPermissionsException {
        if(hasAccessToTransaction(user, id)) {
            return transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        } else {
            throw new MissingPermissionsException(String.format("The user %d (%s) does not has read-access to the transaction %d ", user.getId(), user.getUsername(), id));
        }
    }

    public long getSumByPlayer(User user, int playerID) throws MissingPermissionsException {
        Player player = playerRepository.getReferenceById(playerID);
        if(hasAccessToWallet(user, playerID)) {
            return transactionRepository.getWalletSumOfPlayer(playerID);
        } else {
            throw new MissingPermissionsException(String.format("The user %d (%s) does not has read-access to wallet of player %d (%s)", user.getId(), user.getUsername(), playerID, player.getName()));
        }
    }

    public List<Transaction> findAllByUserID(User user, int playerID) throws MissingPermissionsException {
        Player player = playerRepository.getReferenceById(playerID);
        boolean hasAccess = hasAccessToWallet(user, playerID);
        if(hasAccess)
            return transactionRepository.findByFromOrToOrderByTimeDesc(player, player);
        throw new MissingPermissionsException(String.format("The user %d (%s) does not has read-access to wallet of player %d (%s)", user.getId(), user.getUsername(), playerID, player.getName()));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Transaction save(User user, TransactionPOJO transactionPOJO) throws MissingPermissionsException, POJOMappingException {
        //Player walletOwner = transactionPOJO.fromID != -1 ? playerRepository.getById(transactionPOJO.fromID) : null;
        boolean isAdmin = user.hasAdminPerms(-1);
        if(transactionPOJO.from == null && transactionPOJO.nameFrom != null) {
            transactionPOJO.from = playerRepository.findByName(transactionPOJO.nameFrom).orElseThrow(EntityNotFoundException::new).getUid();
        }
        if(transactionPOJO.to == null && transactionPOJO.nameTo != null) {
            transactionPOJO.to = playerRepository.findByName(transactionPOJO.nameTo).orElseThrow(EntityNotFoundException::new).getUid();
        }
        if(transactionPOJO.from != null && user.ownsWallet(transactionPOJO.from) || isAdmin) {
            if(transactionPOJO.isVerified() && !isAdmin)
                throw new MissingPermissionsException("Missing permissions to verify transactions!");
            Transaction transaction;
            if(transactionPOJO.tid != null) {
                transaction = transactionRepository.findById(transactionPOJO.tid).orElseThrow(EntityNotFoundException::new);
                if (transaction.isVerified() && !isAdmin)
                    throw new MissingPermissionsException("Missing permissions to edit verified transactions!");
            }
            transaction = (Transaction) POJOMapper.map(transactionPOJO);
            transaction.setFrom(transactionPOJO.getFrom() != -1 ? playerRepository.findById(transactionPOJO.getFrom()).orElseThrow(EntityNotFoundException::new) : null);
            transaction.setTo(transactionPOJO.getTo() != -1 ? playerRepository.findById(transactionPOJO.getTo()).orElseThrow(EntityNotFoundException::new) : null);
            if(transaction.getFrom() == null && transaction.getTo() == null)
                throw new UnsupportedOperationException("The transaction need to have at least one player!");
            return transactionRepository.save(transaction);
        } else {
            throw new MissingPermissionsException(String.format("The user %d (%s) does not has access to wallet %d", user.getId(), user.getUsername(), transactionPOJO.getFrom()));
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
        Player player = playerID == -1 ? null : playerRepository.findById(playerID).orElse(null);
        boolean hasAccess = user.hasAdminPerms(-1);
        if(!hasAccess) {
            for (Player p : user.getPlayers()) {
                if (
                        p.getCorp() != null && player != null && player.getCorp() != null &&
                        p.getCorp().equals(player.getCorp())
                ) {
                    hasAccess = true;
                    break;
                }
            }
        }
        return hasAccess;
    }

    public boolean hasAccessToTransaction(User user, int transactionID) {
        Transaction transaction = transactionRepository.findById(transactionID).orElseThrow(EntityNotFoundException::new);
        Player player = transaction.getFrom().isNew() ? null : transaction.getFrom();
        return hasAccessToWallet(user, player == null ? -1 : player.getUid());
    }
}
