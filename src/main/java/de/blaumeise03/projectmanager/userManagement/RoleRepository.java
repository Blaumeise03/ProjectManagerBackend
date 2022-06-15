package de.blaumeise03.projectmanager.userManagement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Override
    void delete(Role role);
}
