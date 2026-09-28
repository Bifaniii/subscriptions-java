package br.com.bifani.subscriptions_java_spring.Services;

import br.com.bifani.subscriptions_java_spring.Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLTMyYnl0ZXMhIQ==";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3_600_000L);
    }

    @Test
    void generateToken_deveUsarEmailComoSubject() {
        var user = User.builder().email("gui@email.com").build();

        String token = jwtService.generateToken(user);

        assertThat(jwtService.extractUsername(token)).isEqualTo("gui@email.com");
    }

    @Test
    void isTokenValid_deveSerVerdadeiroParaODonoDoToken() {
        var user = User.builder().email("gui@email.com").build();

        assertThat(jwtService.isTokenValid(jwtService.generateToken(user), user)).isTrue();
    }

    @Test
    void isTokenValid_deveSerFalsoParaOutroUsuario() {
        var dono = User.builder().email("gui@email.com").build();
        var outro = User.builder().email("outro@email.com").build();

        assertThat(jwtService.isTokenValid(jwtService.generateToken(dono), outro)).isFalse();
    }

    @Test
    void extractUsername_deveRejeitarTokenExpirado() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1_000L);
        String expirado = jwtService.generateToken(User.builder().email("gui@email.com").build());

        assertThatThrownBy(() -> jwtService.extractUsername(expirado))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
