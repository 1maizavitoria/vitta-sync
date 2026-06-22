package br.com.vittasync.vittasync.Security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CorsConfigTest {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    void testCorsConfigurationSourceBean() {
        assertThat(corsConfigurationSource).isNotNull();

        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) corsConfigurationSource;
        CorsConfiguration config = source.getCorsConfigurations().get("/**");

        assertThat(config.getAllowedOrigins()).contains("http://localhost:5173");
        assertThat(config.getAllowedMethods()).contains("GET", "POST", "PUT", "DELETE", "OPTIONS");
        assertThat(config.getAllowedHeaders()).contains("*");
        assertThat(config.getExposedHeaders()).contains("Content-Type", "Content-Disposition");
        assertThat(config.getAllowCredentials()).isTrue();
    }
}
