package br.com.student.portal.entity.enums;

/**
 * Define as categorias de materiais disponíveis
 */
public enum MaterialCategory {
    PDF("pdf"),
    VIDEO("video"),
    ARTICLE("article"),
    PRESENTATION("presentation"),
    DOCUMENT("document"),
    IMAGE("image"),
    AUDIO("audio"),
    OTHER("other");

    private final String value;

    MaterialCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Obtém o enum a partir de uma String
     */
    public static MaterialCategory fromString(String value) {
        for (MaterialCategory category : MaterialCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        return OTHER; // Default
    }
}