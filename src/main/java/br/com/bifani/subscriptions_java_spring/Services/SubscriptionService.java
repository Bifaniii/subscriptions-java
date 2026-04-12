package br.com.bifani.subscriptions_java_spring.Services;

import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import br.com.bifani.subscriptions_java_spring.Repositories.ISubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final ISubscriptionRepository repository;

    public SubscriptionService(ISubscriptionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        return repository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions() {
        return repository.findAll();
    }

    public Subscription getSubscriptionById(UUID id) {
        return repository.findById(id).
                orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }

    public Subscription updateSubscription(UUID id, Subscription updatedSubscription) {
        Subscription existingSubscription = getSubscriptionById(id);
        existingSubscription.setSubscriptionType(updatedSubscription.getSubscriptionType());
        existingSubscription.setPrice(updatedSubscription.getPrice());
        return repository.save(existingSubscription);
    }

    public void deleteSubscription(UUID id) {
        Subscription existingSubscription = getSubscriptionById(id);
        repository.delete(existingSubscription);
    }


}
