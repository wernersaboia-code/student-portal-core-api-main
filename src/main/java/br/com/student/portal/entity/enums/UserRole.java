package br.com.student.portal.entity.enums;

/**
 * Define os papéis/roles disponíveis no sistema
 *
 * SUPER_USER: Acesso total ao sistema
 * ADMIN: Gerencia usuários e conteúdo
 * TEACHER: Cria e gerencia materiais e questões
 * STUDENT: Acessa conteúdo e responde questões
 */
public enum UserRole {
    SUPER_USER("SUPER_USER"),
    ADMIN("ADMIN"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Obtém o enum a partir de uma String
     */
    public static UserRole fromString(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return STUDENT; // Default
    }
}