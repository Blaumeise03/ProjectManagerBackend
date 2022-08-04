package de.blaumeise03.projectmanager.data.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @SuppressWarnings("SpellCheckingInspection")
    @Query(nativeQuery = true, value = "DELETE FROM db_project_content WHERE projectid = ?1 AND id NOT IN ?2")
    void deleteContents(Long projectID, List<Long> exclude);
}
