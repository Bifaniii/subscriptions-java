package br.com.bifani.subscriptions_java_spring.Repositories;

import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<List<Subscription>> findBySubscriptionType(String subscriptionType);
}
