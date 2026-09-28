package br.com.bifani.subscriptions_java_spring.Services;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.AuthRequest;
import br.com.bifani.subscriptions_java_spring.Entities.DTOs.AuthResponse;
import br.com.bifani.subscriptions_java_spring.Entities.DTOs.RegisterRequest;
import br.com.bifani.subscriptions_java_spring.Entities.User;
import br.com.bifani.subscriptions_java_spring.Repositories.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_deveSalvarComSenhaCriptografadaERetornarToken() {
        when(passwordEncoder.encode("123456")).thenReturn("hash-bcrypt");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(new RegisterRequest("Guilherme", "gui@email.com", "123456"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("hash-bcrypt");
        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void login_deveAutenticarERetornarToken() {
        var user = User.builder().email("gui@email.com").build();
        when(userRepository.findByEmail("gui@email.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new AuthRequest("gui@email.com", "123456"));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("gui@email.com", "123456"));
        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void login_naoDeveGerarTokenQuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(new AuthRequest("gui@email.com", "errada")))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(jwtService);
    }
}
