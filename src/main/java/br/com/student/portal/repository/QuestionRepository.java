package br.com.student.portal.repository;

import br.com.student.portal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query("SELECT q FROM Question q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(q.content) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Question> searchByTerm(@Param("term") String term);
    List<Question> findAllByOrderByCreatedAtDesc();
}