package br.com.student.portal.dto.request;

public class AuthRequest {
    private String registration;
    private String password;

    public AuthRequest() {}

    public AuthRequest(String registration, String password) {
        this.registration = registration;
        this.password = password;
    }

    public String getRegistration() { return registration; }
    public void setRegistration(String registration) { this.registration = registration; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
