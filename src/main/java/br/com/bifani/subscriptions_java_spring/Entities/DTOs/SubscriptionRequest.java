package br.com.bifani.subscriptions_java_spring.Entities.DTOs;

import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SubscriptionRequest(@Valid SubscriptionEnum subscriptionType,
                                  @Positive BigDecimal price) {}
