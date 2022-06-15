package de.blaumeise03.projectmanager.toDo;

import de.blaumeise03.projectmanager.exceptions.MissingPermissionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping
    public List<ToDo> findAll(){
        return toDoService.findAll();
    }

    @GetMapping("/{id}")
    public ToDo findById(@PathVariable String id){
        return toDoService.findById(id);
    }

    @PostMapping
    public ToDo create(Authentication authentication, @RequestBody ToDo toDo) throws MissingPermissionsException {
        //System.out.println(principal);
        System.out.println(toDo);
        if(authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("WRITE_PRIVILEGE"))) {
            return toDoService.save(toDo);
        } else {
            throw new MissingPermissionsException();
        }
    }

    @PutMapping("/{id}")
    public ToDo update(@RequestBody ToDo toDo){
        System.out.println(toDo);
        return toDoService.save(toDo);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        toDoService.deleteById(id);
    }

}
