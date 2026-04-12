package br.com.bifani.subscriptions_java_spring.Repositories;

import br.com.bifani.subscriptions_java_spring.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID>{
    User findByEmail(String email);
}
