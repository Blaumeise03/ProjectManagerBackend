package de.blaumeise03.projectmanager.userManagement;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User registerNewUserAccount(UserPOJO userPOJO);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    User findUserByEmail(String email);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password);

    List<String> getUsersFromSessionRegistry();
}
