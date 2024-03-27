package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    
    @BeforeEach
    public void init() {
    	userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Delete a user through the service (checking the delete function of the repository is called)")
    public void testDeleteUser() {
        // Mock user ID
        Long userId = 2L;

        // Perform the delete operation
        userService.delete(userId);

        // Verify that userRepository.deleteById() was called with the correct ID
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Retrieve a User correctly through the service")
    public void testFindById_ExistingId_ReturnsUser() {
        // Mock data
        Long userId = 1L;
        User user = new User(userId, "test@example.com", "Doe", "John", "password", false, LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the service method
        User result = userService.findById(userId);

        // Assertions
        assertThat(result).isNotNull();
        assertThat("test@example.com").isEqualTo(result.getEmail());
    }

    @Test
    @DisplayName("Checking we get NULL when trying to retrieve a non-existing user")
    public void testFindById_NonExistingId_ReturnsNull() {
        // Mock data
        Long userId = 999L;

        // Mock the repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the service method
        User result = userService.findById(userId);

        // Assertions
        assertThat(result).isNull();
    }
}
