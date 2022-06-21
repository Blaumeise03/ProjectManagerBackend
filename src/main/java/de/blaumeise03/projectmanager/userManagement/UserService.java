package de.blaumeise03.projectmanager.userManagement;

import de.blaumeise03.projectmanager.accounting.Player;
import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import de.blaumeise03.projectmanager.exceptions.UserAlreadyExistException;
import de.blaumeise03.projectmanager.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private Environment env;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";

    // API

    @Override
    public User registerNewUserAccount(final UserPOJO userPOJO) {
        if (emailExists(userPOJO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userPOJO.getUsername());
        }
        final User user = new User();

        user.setUsername(userPOJO.getUsername());
        user.setPassword(passwordEncoder.encode(userPOJO.getPassword()));
        user.setEmail(userPOJO.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        userRepository.delete(user);
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
                .stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false)
                        .isEmpty())
                .map(o -> {
                    if (o instanceof User) {
                        return ((User) o).getEmail();
                    } else {
                        return o.toString();
                    }
                }).collect(Collectors.toList());
    }

    public UserSessionInfoPOJO getFromSession(Authentication authentication) throws MissingPermissionsException {
        User user = AuthenticationUtils.getUser(authentication, this);
        UserSessionInfoPOJO userSessionInfoPOJO = new UserSessionInfoPOJO();
        userSessionInfoPOJO.setUid(user.getId());
        userSessionInfoPOJO.setName(user.getUsername());
        for(Player p : user.getPlayers()) {
            if(p.getParent() == null) {
                userSessionInfoPOJO.setMid(p.getUid());
                userSessionInfoPOJO.setMainCharName(p.getName());
                if(p.getCorp() != null && !p.getCorp().isNew()) {
                    userSessionInfoPOJO.setCid(p.getCorp().getCid());
                    userSessionInfoPOJO.setCTag(p.getCorp().getTag());
                }
                break;
            }
        }
        return userSessionInfoPOJO;
    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}