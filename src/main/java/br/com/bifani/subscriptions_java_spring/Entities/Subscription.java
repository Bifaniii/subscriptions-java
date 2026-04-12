package br.com.bifani.subscriptions_java_spring.Entities;

import br.com.bifani.subscriptions_java_spring.Entities.Enums.SubscriptionEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionEnum subscriptionType;

    @OneToOne(mappedBy = "subscription")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal price;

    public boolean isActive() {
        return endDate.isAfter(LocalDateTime.now());
    }

    public boolean isExpired() {
        return endDate.isBefore(LocalDateTime.now());
    }
}
