package br.com.student.portal.controller;

import br.com.student.portal.entity.Task;
import br.com.student.portal.service.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.apache.tomcat.websocket.Constants.FOUND;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Task>> findTaskById(@PathVariable UUID id) {
        return ResponseEntity.status(FOUND).body(taskService.findTaskById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.status(CREATED).body(taskService.createTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> taskDelete(@PathVariable UUID id) {
        taskService.taskDelete(id);
        return ResponseEntity.noContent().build();
    }
}
