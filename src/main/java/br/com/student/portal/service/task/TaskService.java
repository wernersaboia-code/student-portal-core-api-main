package br.com.student.portal.service.task;

import br.com.student.portal.entity.Task;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static br.com.student.portal.validation.TaskValidator.validateTaskFields;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Optional<Task> findTaskById(UUID id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException("There's no task with this id");
        }
        return taskRepository.findById(id);

    }

    public Task createTask(Task task) {

        validateTaskFields(task);

        return taskRepository.save(task);
    }

    public void taskDelete(UUID id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException("There's no task with this id");
        }
        taskRepository.deleteById(id);
    }

}
