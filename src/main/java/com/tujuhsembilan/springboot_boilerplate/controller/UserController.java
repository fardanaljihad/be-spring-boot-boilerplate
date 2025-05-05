package com.tujuhsembilan.springboot_boilerplate.controller;

import jakarta.validation.Valid; // Use jakarta validation
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tujuhsembilan.springboot_boilerplate.dto.CreateUserRequest;
import com.tujuhsembilan.springboot_boilerplate.dto.UserDto;
import com.tujuhsembilan.springboot_boilerplate.model.entity.User;
import com.tujuhsembilan.springboot_boilerplate.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Marks this as a REST controller
@RequestMapping("/api/v1/users") // Base path for all endpoints in this controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    // GET /api/v1/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Received request to get all users");
        List<UserDto> userDtos = userService.getAllUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // GET /api/v1/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Received request to get user by id: {}", id);
        User user = userService.getUserByIdOrThrow(id);
        return ResponseEntity.ok(convertToDto(user));
    }

    // POST /api/v1/users
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Received request to create user: {}", createUserRequest.getUsername());
        User newUser = new User();
        newUser.setUsername(createUserRequest.getUsername());
        newUser.setEmail(createUserRequest.getEmail());
        newUser.setPassword(createUserRequest.getPassword()); // HASH THIS in service!

        User createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdUser));
    }

    // PUT /api/v1/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
            @Valid @RequestBody CreateUserRequest userDetails) {
        log.info("Received request to update user: {}", id);
        User userToUpdate = new User();
        userToUpdate.setEmail(userDetails.getEmail());

        User updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(convertToDto(updatedUser));
    }

    // DELETE /api/v1/users/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user: {}", id);
        userService.deleteUser(id);
    }
}