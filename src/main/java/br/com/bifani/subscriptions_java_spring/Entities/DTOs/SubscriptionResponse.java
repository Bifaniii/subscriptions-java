package br.com.bifani.subscriptions_java_spring.Entities.DTOs;

import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;
import br.com.bifani.subscriptions_java_spring.Entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResponse(UUID id,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate,
                                   SubscriptionEnum subscriptionType,
                                   User user,
                                   BigDecimal price) {}
