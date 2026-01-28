package br.com.student.portal.repository;

import br.com.student.portal.entity.Material;
import br.com.student.portal.entity.enums.MaterialCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository para Material
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {

    /**
     * Buscar materiais por categoria
     */
    List<Material> findByCategory(MaterialCategory category);

    /**
     * Buscar materiais por nome ou descrição
     */
    @Query("SELECT m FROM Material m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(m.description) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "ORDER BY m.uploadDate DESC")
    List<Material> searchByName(@Param("term") String term);

    /**
     * Buscar todos os materiais ordenados por data (mais recentes primeiro)
     */
    @Query("SELECT m FROM Material m ORDER BY m.uploadDate DESC")
    List<Material> findAllOrderByNewest();

    /**
     * Buscar materiais de um usuário específico
     */
    @Query("SELECT m FROM Material m WHERE m.uploadedBy.id = :userId ORDER BY m.uploadDate DESC")
    List<Material> findByUploadedById(@Param("userId") UUID userId);

    /**
     * Buscar materiais mais baixados (com paginação)
     */
    Page<Material> findAllByOrderByDownloadsDesc(Pageable pageable);
}