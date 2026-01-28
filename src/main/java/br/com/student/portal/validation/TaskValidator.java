package br.com.student.portal.validation;

import br.com.student.portal.entity.Task;
import br.com.student.portal.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskValidator {
    //TODO: CRIAR UMA FUNÇÃO PARA MONTAR O ERRO DE BAD REQUEST PADRÃO PARA TODAS AS FUNÇÕES DO VALIDATOR, PODE OLHAR NA CLASSE DE STUDENT VALIDATOR
    public static void validateName(String name) {
        if (name.isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        }

    }

    public static void validateDeadLine(LocalDateTime deadline) {
        if (deadline == null) {
            throw new BadRequestException("Date can't be null");
        }
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Date can't be in the paste");
        }
    }

    public static void validateDescription(String description) {
        if (description.isEmpty()) {
            throw new BadRequestException("Description can't be empty");
        }
    }

    public static void validateTitle(String title) {
        if (title.isEmpty()) {
            throw new BadRequestException("Title can't be empty");
        }
    }

    public static void validateCourse(String course) {
        if (course.isEmpty()) {
            throw new BadRequestException("Course can't be empty");
        }
    }

    public static void validateTaskFields(Task task) {
        validateName(task.getName());
        validateDeadLine(task.getDeadline());
        validateDescription(task.getDescription());
        validateTitle(task.getTitle());
        validateCourse(task.getCourse());
    }
}
