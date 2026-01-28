package br.com.student.portal.dto.response;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String userName;

    public AuthResponse(String token, UUID userId, String userName) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
    }

    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getUserName() { return userName; }
}
