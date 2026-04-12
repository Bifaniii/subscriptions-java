package br.com.bifani.subscriptions_java_spring.Controllers;

import br.com.bifani.subscriptions_java_spring.Entities.DTOs.ResponseSub;
import br.com.bifani.subscriptions_java_spring.Repositories.ISubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final ISubscriptionRepository repository;

    public SubscriptionController(ISubscriptionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ResponseSub>> getAllSubscriptions() {
        List<ResponseSub> subscriptions = repository.findAll().stream()
                .map(sub -> new ResponseSub(
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
}
