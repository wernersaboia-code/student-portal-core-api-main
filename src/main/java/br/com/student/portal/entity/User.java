package br.com.student.portal.entity;

import br.com.student.portal.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entidade User consolidada
 * Representa qualquer usuário do sistema: Administrador, Professor ou Estudante
 *
 * O papel é definido pelo campo 'role'
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Número de matrícula/registro do usuário
     * Usado para estudantes e professores
     */
    @Column(unique = true, length = 255)
    private String registration;

    /**
     * Define se o usuário pode acessar o sistema
     */
    @Column(nullable = false)
    private Boolean accessEnable = true;

    /**
     * Timestamps automáticos
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Construtor para criação rápida com dados essenciais
     */
    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accessEnable = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Construtor com matrícula
     */
    public User(String name, String email, String password, String registration, UserRole role) {
        this(name, email, password, role);
        this.registration = registration;
    }

    /**
     * Prepopulate timestamps antes de salvar
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ============ UserDetails Implementation ============

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case SUPER_USER -> List.of(
                    new SimpleGrantedAuthority("ROLE_SUPER_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case TEACHER -> List.of(
                    new SimpleGrantedAuthority("ROLE_TEACHER"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case STUDENT -> List.of(
                    new SimpleGrantedAuthority("ROLE_STUDENT"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        };
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accessEnable;
    }

    // ============ Utility Methods ============

    /**
     * Verifica se é administrador
     */
    public boolean isAdmin() {
        return role == UserRole.ADMIN || role == UserRole.SUPER_USER;
    }

    /**
     * Verifica se é professor
     */
    public boolean isTeacher() {
        return role == UserRole.TEACHER;
    }

    /**
     * Verifica se é estudante
     */
    public boolean isStudent() {
        return role == UserRole.STUDENT;
    }
}