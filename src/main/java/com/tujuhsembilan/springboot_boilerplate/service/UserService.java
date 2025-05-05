package com.tujuhsembilan.springboot_boilerplate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.tujuhsembilan.springboot_boilerplate.exception.ResourceNotFoundException;
import com.tujuhsembilan.springboot_boilerplate.model.entity.User;
import com.tujuhsembilan.springboot_boilerplate.repository.jpa.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public User getUserByIdOrThrow(Long id) {
        log.info("Fetching user by id (or throw): {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByUsernameOrThrow(String username) {
        log.info("Fetching user by username (or throw): {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        log.info("Creating new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @CachePut(value = "users", key = "#result.id")
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserByIdOrThrow(id);

        existingUser.setEmail(userDetails.getEmail());

        existingUser.setEnabled(userDetails.isEnabled());
        log.info("Updating user with id: {}", id);

        return existingUser;
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        log.warn("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }
}
