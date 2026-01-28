package br.com.student.portal.service.question;

import br.com.student.portal.dto.request.AnswerRequest;
import br.com.student.portal.dto.response.AnswerResponse;
import br.com.student.portal.entity.Answer;
import br.com.student.portal.entity.Question;
import br.com.student.portal.entity.User;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.repository.AnswerRepository;
import br.com.student.portal.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerResponse getAnswerById(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Resposta não encontrada com ID: " + id));
        return mapToResponse(answer);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswersByQuestionId(UUID questionId) {
        return answerRepository.findByQuestionId(questionId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AnswerResponse createAnswer(AnswerRequest request, UUID questionId, User author) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ObjectNotFoundException("Pergunta não encontrada com ID: " + questionId));

        Answer answer = Answer.builder()
                .content(request.getContent())
                .author(author)
                .question(question)
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        question.setAnswerCount(question.getAnswerCount() + 1);
        questionRepository.save(question);

        log.info("Resposta criada com ID: {} para pergunta: {}", savedAnswer.getId(), questionId);
        return mapToResponse(savedAnswer);
    }

    public AnswerResponse updateAnswer(UUID id, AnswerRequest request, User author) {
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Resposta não encontrada com ID: " + id));

        if (!existingAnswer.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("Apenas o autor pode editar a resposta");
        }

        existingAnswer.setContent(request.getContent());
        Answer updatedAnswer = answerRepository.save(existingAnswer);

        return mapToResponse(updatedAnswer);
    }

    public void deleteAnswer(UUID id, User requester) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Resposta não encontrada com ID: " + id));

        boolean isAuthor = answer.getAuthor().getId().equals(requester.getId());
        boolean isAdmin = requester.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("Não autorizado a deletar esta resposta");
        }

        Question question = answer.getQuestion();
        question.setAnswerCount(Math.max(0, question.getAnswerCount() - 1));
        questionRepository.save(question);

        answerRepository.delete(answer);
    }

    @Transactional(readOnly = true)
    public long countAnswersByQuestionId(UUID questionId) {
        return answerRepository.countByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswersByAuthor(UUID authorId) {
        return answerRepository.findByAuthorId(authorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AnswerResponse mapToResponse(Answer entity) {
        return AnswerResponse.builder()
                .id(entity.getId().toString())
                .questionId(entity.getQuestion().getId().toString())
                .authorId(entity.getAuthor().getId().toString())
                .authorName(entity.getAuthor().getName())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : "")
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : "")
                .build();
    }
}