package br.com.student.portal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponse {

    private String id;
    private String name;
    private String description;
    private String filename;
    private String category;
    private String uploaderId;
    private String uploaderName;
    private Long downloads;
    private String uploadDate;
}