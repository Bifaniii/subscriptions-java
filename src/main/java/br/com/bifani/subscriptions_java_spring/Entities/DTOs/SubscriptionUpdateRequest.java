package br.com.bifani.subscriptions_java_spring.Entities.DTOs;

import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;

import java.math.BigDecimal;

public record SubscriptionUpdateRequest(SubscriptionEnum subscriptionType,
                                        BigDecimal price) {
}
