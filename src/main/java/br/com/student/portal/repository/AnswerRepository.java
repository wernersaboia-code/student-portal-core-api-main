package br.com.student.portal.repository;

import br.com.student.portal.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId ORDER BY a.createdAt ASC")
    List<Answer> findByQuestionId(@Param("questionId") UUID questionId);

    @Query("SELECT a FROM Answer a WHERE a.author.id = :authorId ORDER BY a.createdAt DESC")
    List<Answer> findByAuthorId(@Param("authorId") UUID authorId);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question.id = :questionId")
    long countByQuestionId(@Param("questionId") UUID questionId);
}