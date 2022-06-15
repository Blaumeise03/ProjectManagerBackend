package de.blaumeise03.projectmanager.toDo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, String> {

}
