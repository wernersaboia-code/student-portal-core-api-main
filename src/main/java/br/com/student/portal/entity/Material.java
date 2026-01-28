package br.com.student.portal.entity;

import br.com.student.portal.entity.enums.MaterialCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Material
 * Representa materiais de estudo (PDFs, vídeos, artigos, etc)
 * Consolidação de MaterialEntity e FileEntity
 */
@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MaterialCategory category;

    @Column(nullable = false, length = 255)
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User uploadedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @Builder.Default
    @Column(nullable = false)
    private Long downloads = 0L;

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
        uploadDate = LocalDateTime.now();
        if (downloads == null) {
            downloads = 0L;
        }
        if (category == null) {
            category = MaterialCategory.OTHER;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Incrementar contador de downloads
     */
    public void incrementDownloads() {
        if (this.downloads == null) {
            this.downloads = 1L;
        } else {
            this.downloads++;
        }
    }
}