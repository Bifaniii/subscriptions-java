package br.com.bifani.subscriptions_java_spring.Controllers;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionRequest;
import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;
import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import br.com.bifani.subscriptions_java_spring.Services.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService service;

    @InjectMocks
    private SubscriptionController controller;

    private MockMvc mockMvc;

    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Subscription subscription() {
        return Subscription.builder()
                .id(id)
                .subscriptionType(SubscriptionEnum.PREMIUM)
                .price(new BigDecimal("39.90"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
    }

    @Test
    void getAllSubscriptions_deveRetornar200() throws Exception {
        when(service.getAllSubscriptions()).thenReturn(List.of(subscription()));

        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subscriptionType").value("PREMIUM"));
    }

    @Test
    void getSubscriptionById_deveRetornarAssinatura() throws Exception {
        when(service.getSubscriptionById(id)).thenReturn(subscription());

        mockMvc.perform(get("/subscriptions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void createSubscription_deveRetornar200QuandoValido() throws Exception {
        when(service.createSubscription(any(SubscriptionRequest.class))).thenReturn(subscription());

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subscriptionType": "PREMIUM", "price": 39.90}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(39.90));
    }

    @Test
    void createSubscription_deveRetornar400QuandoPrecoNegativo() throws Exception {
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"subscriptionType": "PREMIUM", "price": -10}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void deleteSubscription_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/subscriptions/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).deleteSubscription(id);
    }
}
