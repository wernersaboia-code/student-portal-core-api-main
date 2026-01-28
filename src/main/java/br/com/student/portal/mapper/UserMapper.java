package br.com.student.portal.mapper;

import br.com.student.portal.dto.request.UserRequest;
import br.com.student.portal.dto.response.UserResponse;
import br.com.student.portal.entity.User;
import br.com.student.portal.entity.enums.UserRole;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre User e DTOs
 */
@Component
public class UserMapper {

    /**
     * Converte UserRequest para User
     * Método compatível com nome antigo: userRequestIntoUser
     */
    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        return new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRegistration(),
                UserRole.fromString(request.getRole() != null ? request.getRole() : "STUDENT")
        );
    }

    /**
     * Alias para compatibilidade - userRequestIntoUser
     */
    public User userRequestIntoUser(UserRequest request) {
        return toEntity(request);
    }

    /**
     * Converte User para UserResponse
     * Método compatível com nome antigo: userIntoUserResponse
     */
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getValue())
                .registration(user.getRegistration())
                .accessEnable(user.getAccessEnable())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Alias para compatibilidade - userIntoUserResponse
     */
    public UserResponse userIntoUserResponse(User user) {
        return toResponse(user);
    }

    /**
     * Atualiza um User existente com dados de UserRequest
     */
    public void updateUserFromRequest(UserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if (request.getRole() != null) {
            user.setRole(UserRole.fromString(request.getRole()));
        }
        if (request.getRegistration() != null) {
            user.setRegistration(request.getRegistration());
        }
    }
}