package br.com.bifani.subscriptions_java_spring.Services;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionRequest;
import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import br.com.bifani.subscriptions_java_spring.Entities.User;
import br.com.bifani.subscriptions_java_spring.Repositories.ISubscriptionRepository;
import br.com.bifani.subscriptions_java_spring.Repositories.IUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final ISubscriptionRepository repository;
    private final IUserRepository userRepository;

    public SubscriptionService(ISubscriptionRepository repository, IUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Subscription createSubscription(SubscriptionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        Subscription sub = Subscription.builder()
                .subscriptionType(request.subscriptionType())
                .price(request.price())
                .startDate(java.time.LocalDateTime.now())
                .endDate(java.time.LocalDateTime.now().plusMonths(1))
                .build();

        user.setSubscription(sub);
        sub.setUser(user);
        userRepository.save(user);

        return sub;
    }

    public List<Subscription> getAllSubscriptions() {
        return repository.findAll();
    }

    public Subscription getSubscriptionById(UUID id) {
        return repository.findById(id).
                orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }

    public void deleteSubscription(UUID id) {
        Subscription existingSubscription = getSubscriptionById(id);
        repository.delete(existingSubscription);
    }


}
