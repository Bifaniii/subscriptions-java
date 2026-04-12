package br.com.bifani.subscriptions_java_spring.Entities;

import br.com.bifani.subscriptions_java_spring.Entities.Enums.UserEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private UserEnum userType;

    @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
    private Subscription subscription;

    public boolean isActive() {
        return subscription != null && subscription.getUser().isActive();
    }
}
