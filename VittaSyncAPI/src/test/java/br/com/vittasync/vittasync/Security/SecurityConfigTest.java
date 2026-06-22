package br.com.vittasync.vittasync.Security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void testSecurityFilterChainBean() {
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void testOpenApiBean() {
        SecurityConfig config = new SecurityConfig(null, null);
        assertThat(config.customOpenAPI()).isNotNull();
        assertThat(config.customOpenAPI().getInfo().getTitle()).isEqualTo("VittaSync API");
        assertThat(config.customOpenAPI().getComponents().getSecuritySchemes()).containsKey("bearerAuth");
    }
}
