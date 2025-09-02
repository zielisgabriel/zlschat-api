package br.com.gabriel.zlschat.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.zlschat.models.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByUsernameStartingWith(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
