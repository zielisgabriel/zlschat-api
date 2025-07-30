package br.com.gabriel.websocket.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.websocket.models.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByUsernameStartingWith(String username);
    boolean existsByEmail(String email);
}
