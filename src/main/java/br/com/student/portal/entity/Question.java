package br.com.student.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Question
 * Representa uma pergunta feita por usuários (estudantes ou professores)
 */
@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Builder.Default
    @Column(nullable = false)
    private Integer answerCount = 0;

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
        if (answerCount == null) {
            answerCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Incrementar contador de respostas
     */
    public void incrementAnswerCount() {
        if (this.answerCount == null) {
            this.answerCount = 1;
        } else {
            this.answerCount++;
        }
    }

    /**
     * Decrementar contador de respostas
     */
    public void decrementAnswerCount() {
        if (this.answerCount != null && this.answerCount > 0) {
            this.answerCount--;
        }
    }
}