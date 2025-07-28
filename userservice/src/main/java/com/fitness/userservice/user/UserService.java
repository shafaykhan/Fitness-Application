package com.fitness.userservice.user;

import com.fitness.userservice.common.dto.UserRequestDTO;
import com.fitness.userservice.common.dto.UserDTO;
import com.fitness.userservice.common.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  private final UserRepository repository;
  private final ModelMapper modelMapper;

  public UserService(UserRepository repository, ModelMapper modelMapper) {
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  public UserDTO registerUser(UserRequestDTO dto) {
    if (repository.existsByIdNotAndEmail(dto.getId(), dto.getEmail()))
      throw new RuntimeException("Email already exists!");
    if (repository.existsByIdNotAndUsername(dto.getId(), dto.getUsername()))
      throw new RuntimeException("Username already exists!");
    if (existsByKeycloakId(dto.getKeycloakId()))
      throw new RuntimeException("KeycloakId already exists!");

    User user = modelMapper.map(dto, User.class);
    User savedUser = repository.save(user);
    return modelMapper.map(savedUser, UserDTO.class);
  }

  public UserDTO updateUser(UserRequestDTO dto) {
    User user;
    if (dto.getId() != null) {
      user = findEntityById(dto.getId());
      dto.setKeycloakId(user.getKeycloakId());

    } else if (dto.getKeycloakId() != null) {
      user = findEntityByKeycloakId(dto.getKeycloakId());
      dto.setId(user.getId());
      dto.setPassword(user.getPassword());

    } else {
      throw new RuntimeException("Invalid update payload!");

    }

    if (repository.existsByIdNotAndEmail(dto.getId(), dto.getEmail()))
      throw new RuntimeException("Email already exists!");
    if (repository.existsByIdNotAndUsername(dto.getId(), dto.getUsername()))
      throw new RuntimeException("Username already exists!");

    modelMapper.map(dto, user);

    User savedUser = repository.save(user);
    return modelMapper.map(savedUser, UserDTO.class);
  }

  public List<UserDTO> findAll() {
    return repository.findAll().stream()
        .map(user -> modelMapper.map(user, UserDTO.class))
        .toList();
  }

  public UserDTO findById(UUID id) {
    User entity = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found!"));

    return modelMapper.map(entity, UserDTO.class);
  }

  public UserDTO findByKeycloakId(String keycloakId) {
    User entity = repository.findByKeycloakId(keycloakId)
        .orElseThrow(() -> new RuntimeException("User not found!"));

    return modelMapper.map(entity, UserDTO.class);
  }

  public boolean existsById(UUID id) {
    return repository.existsById(id);
  }

  public boolean existsByKeycloakId(String keycloakId) {
    return repository.existsByKeycloakId(keycloakId);
  }

  private User findEntityById(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found!"));
  }

  private User findEntityByKeycloakId(String keycloakId) {
    return repository.findByKeycloakId(keycloakId)
        .orElseThrow(() -> new RuntimeException("User not found!"));
  }
}
