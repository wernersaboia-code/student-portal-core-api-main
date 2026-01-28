package br.com.student.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private String id;
    private String questionId;
    private String authorId;
    private String authorName;
    private String content;
    private String createdAt;
    private String updatedAt;
}