package br.com.bifani.subscriptions_java_spring.Services;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionRequest;
import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionUpdateRequest;
import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;
import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import br.com.bifani.subscriptions_java_spring.Entities.User;
import br.com.bifani.subscriptions_java_spring.Repositories.ISubscriptionRepository;
import br.com.bifani.subscriptions_java_spring.Repositories.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private ISubscriptionRepository repository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private SubscriptionService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticar(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, List.of()));
    }

    private Subscription subscription(UUID id) {
        return Subscription.builder()
                .id(id)
                .subscriptionType(SubscriptionEnum.BASIC)
                .price(new BigDecimal("19.90"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
    }

    @Test
    void createSubscription_deveVincularAssinaturaDeUmMesAoUsuarioLogado() {
        var user = User.builder().email("gui@email.com").build();
        autenticar("gui@email.com");
        when(userRepository.findByEmail("gui@email.com")).thenReturn(Optional.of(user));

        Subscription sub = service.createSubscription(
                new SubscriptionRequest(SubscriptionEnum.PREMIUM, new BigDecimal("39.90")));

        assertThat(sub.getUser()).isEqualTo(user);
        assertThat(user.getSubscription()).isEqualTo(sub);
        assertThat(sub.getEndDate()).isCloseTo(LocalDateTime.now().plusMonths(1), within(5, java.time.temporal.ChronoUnit.SECONDS));
        assertThat(user.isActive()).isTrue();
        verify(userRepository).save(user);
    }

    @Test
    void createSubscription_deveFalharQuandoUsuarioLogadoNaoExiste() {
        autenticar("fantasma@email.com");
        when(userRepository.findByEmail("fantasma@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createSubscription(
                new SubscriptionRequest(SubscriptionEnum.BASIC, BigDecimal.TEN)))
                .hasMessageContaining("fantasma@email.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getSubscriptionById_deveLancarExcecaoQuandoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSubscriptionById(id)).hasMessageContaining(id.toString());
    }

    @Test
    void updateSubscription_deveAlterarTipoEPreco() {
        UUID id = UUID.randomUUID();
        Subscription existing = subscription(id);
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Subscription updated = service.updateSubscription(id,
                new SubscriptionRequest(SubscriptionEnum.VIP, new BigDecimal("99.90")));

        assertThat(updated.getSubscriptionType()).isEqualTo(SubscriptionEnum.VIP);
        assertThat(updated.getPrice()).isEqualByComparingTo("99.90");
    }

    @Test
    void patchSubscription_deveManterCamposNaoInformados() {
        UUID id = UUID.randomUUID();
        Subscription existing = subscription(id);
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Subscription patched = service.patchSubscription(id, new SubscriptionUpdateRequest(null, new BigDecimal("24.90")));

        assertThat(patched.getSubscriptionType()).isEqualTo(SubscriptionEnum.BASIC);
        assertThat(patched.getPrice()).isEqualByComparingTo("24.90");
    }

    @Test
    void deleteSubscription_deveRemoverAssinaturaExistente() {
        UUID id = UUID.randomUUID();
        Subscription existing = subscription(id);
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        service.deleteSubscription(id);

        verify(repository).delete(existing);
    }

    @Test
    void isExpired_deveSerVerdadeiroQuandoDataFinalJaPassou() {
        Subscription sub = subscription(UUID.randomUUID());
        sub.setEndDate(LocalDateTime.now().minusDays(1));

        assertThat(sub.isExpired()).isTrue();
        assertThat(sub.isActive()).isFalse();
    }
}
