package com.tujuhsembilan.springboot_boilerplate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tujuhsembilan.springboot_boilerplate.exception.ResourceNotFoundException;
import com.tujuhsembilan.springboot_boilerplate.model.entity.User;
import com.tujuhsembilan.springboot_boilerplate.repository.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "testuser", "test@example.com", "password", true, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Test Get User By Id - Success")
    void givenUserId_whenGetUserById_thenReturnUserObject() {
        given(userRepository.findById(1L)).willReturn(Optional.of(sampleUser));

        User foundUser = userService.getUserByIdOrThrow(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1L);
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Get User By Id - Not Found")
    void givenNonExistentUserId_whenGetUserById_thenThrowsResourceNotFoundException() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByIdOrThrow(99L);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found with id: 99");
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Test Create User - Success")
    void givenUserObject_whenCreateUser_thenReturnSavedUser() {
        given(userRepository.existsByUsername(sampleUser.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(sampleUser.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            userToSave.setCreatedAt(LocalDateTime.now());
            return userToSave;
        });

        User createdUser = userService.createUser(sampleUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(sampleUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Create User - Username Exists")
    void givenExistingUsername_whenCreateUser_thenThrowsIllegalArgumentException() {
        given(userRepository.existsByUsername(sampleUser.getUsername())).willReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(sampleUser);
        });

        assertThat(exception.getMessage()).contains("Username already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Delete User - Success")
    void givenUserId_whenDeleteUser_thenDeletesUser() {
        long userId = 1L;
        given(userRepository.existsById(userId)).willReturn(true);
        willDoNothing().given(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> {
            userService.deleteUser(userId);
        });

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Test Delete User - Not Found")
    void givenNonExistentUserId_whenDeleteUser_thenThrowsResourceNotFoundException() {
        long userId = 99L;
        given(userRepository.existsById(userId)).willReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong());
    }

}
