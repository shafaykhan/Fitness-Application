package com.fitness.userservice.user;

import com.fitness.userservice.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Boolean existsByKeycloakId(String keycloakId);

  Boolean existsByIdNotAndEmail(UUID id, String email);

  Boolean existsByIdNotAndUsername(UUID id, String username);

  Optional<User> findByKeycloakId(String keycloakId);
}
