package de.blaumeise03.projectmanager.userManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //System.out.println("Start");
        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = userRepository.findByUsername("admin");
        if(user==null) {
            user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("test@test.com");
            user.setRoles(Arrays.asList(adminRole));
            user.setEnabled(true);
            user.setId(0L);
            userRepository.save(user);
        }

        Role userRole = roleRepository.findByName("ROLE_USER");
        User user2 = userRepository.findByUsername("user");
        if(user2 == null) {
            user2 = new User();
            user2.setUsername("user");
            user2.setPassword(passwordEncoder.encode("user"));
            user2.setEmail("test@test.com");
            user2.setRoles(Arrays.asList(userRole));
            user2.setEnabled(true);
            user2.setId(1L);
            userRepository.save(user2);
        }
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        //System.out.println(name);
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
            //System.out.println("saved privilege");
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        //System.out.println("========================");
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
            //System.out.println("new " + privileges.size() + " " + role.getPrivileges().size());
        }
        //System.out.println(role.getPrivileges());
        if(role.getPrivileges() != null && role.getPrivileges().isEmpty()) {
            role.getPrivileges().addAll(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
