package br.com.student.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Task
 * Representa uma tarefa/atividade para estudantes
 */
@Entity
@Table(name = "task_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 255)
    private String title;

    @Builder.Default
    @Column(nullable = false)
    private Boolean received = false;

    @Column(nullable = false, length = 255)
    private String course;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Prepopulate timestamps antes de salvar
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (received == null) {
            received = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se a tarefa está atrasada
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(deadline);
    }

    /**
     * Marca tarefa como recebida
     */
    public void markAsReceived() {
        this.received = true;
    }
}