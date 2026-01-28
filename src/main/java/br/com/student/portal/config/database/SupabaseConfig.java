package br.com.student.portal.config.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import lombok.Data;

@Configuration
@Profile("supabase")
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class SupabaseConfig {

    private String url;
    private String username;
    private String password;

    /**
     * Valida a configuração do Supabase na inicialização
     */
    public void validateSupabaseConfiguration() {
        if (url == null || url.isEmpty() || url.contains("[seu-projeto]")) {
            throw new IllegalArgumentException(
                    "❌ Supabase não está configurado corretamente!\n" +
                            "Por favor, atualize o arquivo 'application-supabase.properties' com seus dados:\n" +
                            "1. spring.datasource.url -> Substituir [seu-projeto] pela URL do Supabase\n" +
                            "2. spring.datasource.password -> Sua senha de acesso PostgreSQL\n\n" +
                            "Como conseguir as informações:\n" +
                            "  → Acesse: https://app.supabase.com\n" +
                            "  → Navegue até: Project > Settings > Database\n" +
                            "  → Copie a Connection String\n"
            );
        }
    }
}