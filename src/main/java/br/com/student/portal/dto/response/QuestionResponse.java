package br.com.student.portal.dto.response;

import br.com.student.portal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private String id;
    private String title;
    private String content;
    private User author;
    private Integer answerCount;
    private String createdAt;
    private String updatedAt;
}