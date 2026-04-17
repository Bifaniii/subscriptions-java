package br.com.bifani.subscriptions_java_spring.Controllers;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionRequest;
import br.com.bifani.subscriptions_java_spring.Entities.DTOs.SubscriptionResponse;
import br.com.bifani.subscriptions_java_spring.Entities.Subscription;
import br.com.bifani.subscriptions_java_spring.Repositories.ISubscriptionRepository;
import br.com.bifani.subscriptions_java_spring.Services.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        List<SubscriptionResponse> subscriptions = service.getAllSubscriptions().stream()
                .map(sub -> new SubscriptionResponse(
                        sub.getId(),
                        sub.getStartDate(),
                        sub.getEndDate(),
                        sub.getSubscriptionType(),
                        sub.getUser(),
                        sub.getPrice()
                ))
                .toList();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable UUID id) {
        Subscription sub = service.getSubscriptionById(id);

        if (sub == null) {
            return ResponseEntity.notFound().build();
        }

        SubscriptionResponse response = new SubscriptionResponse(
                sub.getId(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getSubscriptionType(),
                sub.getUser(),
                sub.getPrice()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        Subscription sub = service.createSubscription(request);
        return ResponseEntity.ok(new SubscriptionResponse(
                sub.getId(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getSubscriptionType(),
                sub.getUser(),
                sub.getPrice()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

}
