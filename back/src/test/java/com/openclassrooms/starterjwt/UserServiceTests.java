package com.openclassrooms.starterjwt;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

class UserServiceTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser() {
        // Mock user ID
        Long userId = 2L;

        // Perform the delete operation
        userService.delete(userId);

        // Verify that userRepository.deleteById() was called with the correct ID
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testFindById_ExistingId_ReturnsUser() {
        // Mock data
        Long userId = 1L;
        User user = new User(userId, "test@example.com", "Doe", "John", "password", false, LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the service method
        User result = userService.findById(userId);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testFindById_NonExistingId_ReturnsNull() {
        // Mock data
        Long userId = 999L;

        // Mock the repository behavior
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the service method
        User result = userService.findById(userId);

        // Assertions
        Assertions.assertNull(result);
    }
}
