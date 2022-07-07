package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.User;
import de.blaumeise03.projectmanager.userManagement.UserPOJO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class POJOMapperTest {

    @Test
    void map() throws POJOMappingException {
        UserPOJO pojoOr = new UserPOJO();
        User expected = new User();
        pojoOr.setUsername("username");
        pojoOr.setPassword("password");
        pojoOr.setEmail("email");
        pojoOr.setEnabled(true);
        pojoOr.setId(1234567890L);
        expected.setUsername("username");
        expected.setPassword("password");
        expected.setEmail("email");
        expected.setEnabled(true);
        expected.setId(1234567890L);
        User user = (User) POJOMapper.map(pojoOr);
        assertEquals(expected, user);
        UserPOJO pojo = (UserPOJO) POJOMapper.map(user);
        assertEquals(pojoOr, pojo);
    }
}