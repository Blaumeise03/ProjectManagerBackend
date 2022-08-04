package de.blaumeise03.projectmanager.data.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectContentRepository extends JpaRepository<ProjectContent, Long> {
    @Query(nativeQuery = true, value = "DELETE FROM db_project_content WHERE id IN ?1")
    void deleteContents(List<Long> delete);
}
