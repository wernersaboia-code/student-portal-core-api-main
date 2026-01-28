package br.com.student.portal.service;

import br.com.student.portal.entity.Task;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.repository.TaskRepository;
import br.com.student.portal.service.task.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.student.portal.data.FixedData.TASK_ID;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @Mock
    Task task;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    TaskService taskService;
    @Before
    public void setup(){
        task = new Task(TASK_ID, "Task", LocalDateTime.now().plusDays(999), "Descrição super foda", "Titulo super foda", false, "Curso foda");

    }

    @Test
    public void mustCreateTask(){
        given(taskRepository.save(task)).willReturn(task);
        var result = taskService.createTask(task);

        assertEquals(TASK_ID, result.getId());
        assertEquals("Task", result.getName());
        assertEquals("Titulo super foda", result.getTitle());
        assertEquals("Descrição super foda", result.getDescription());
        assertEquals(false, result.isReceived());
        assertEquals("Curso foda", result.getCourse());
    }
    @Test
    public void mustFindTaskById(){
        given(taskRepository.findById(TASK_ID)).willReturn(Optional.ofNullable(task));
        var  result = taskService.findTaskById(TASK_ID);

        assertEquals("Task", result.get().getName());
        assertEquals("Titulo super foda", result.get().getTitle());
        assertEquals("Descrição super foda", result.get().getDescription());
        assertEquals(false, result.get().isReceived());
        assertEquals("Curso foda", result.get().getCourse());

    }
    @Test
    public void mustNotFindTask() {
        assertThrows(ObjectNotFoundException.class, () -> {
            taskService.findTaskById(TASK_ID);
        });
    }
    @Test
    public void mustDeleteTask(){
        given(taskRepository.findById(TASK_ID)).willReturn(Optional.ofNullable(task));
        taskService.taskDelete(TASK_ID);
        verify(taskRepository, times(1)).deleteById(TASK_ID);
    }
    @Test
    public void mustNotFindTaskOnDelete(){
        assertThrows(ObjectNotFoundException.class, () ->{
            taskService.taskDelete(TASK_ID);
        });

    }
}


