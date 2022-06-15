package de.blaumeise03.projectmanager.toDo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ToDo {
    @Id
    private String id;

    private String title;

    private Boolean completed;

    public ToDo() {
        id = Double.toHexString(Math.random());
    }

    public ToDo(String title, Boolean completed){
        this.title = title;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
