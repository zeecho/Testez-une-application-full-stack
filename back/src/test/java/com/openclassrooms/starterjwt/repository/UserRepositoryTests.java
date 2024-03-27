package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

class UserRepositoryTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    @DisplayName("Get an existing user using their email")
    public void testFindByEmail_ExistingEmail_ReturnsUser() {
        String email = "test@example.com";
        User user = new User(1L, email, "Doe", "John", "password", false, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail(email);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user.getEmail(), result.get().getEmail());
    }
    

    @Test
    @DisplayName("Get a non existing user using their email (should return empty)")
    public void testFindByEmail_NonExistingEmail_ReturnsEmpty() {
        String email = "nonexisting@example.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmail(email);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Check if an email already exists (true)")
    public void testExistsByEmail_ExistingEmail_ReturnsTrue() {
        String email = "test@example.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userRepository.existsByEmail(email);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Check if an email already exists (false)")
    public void testExistsByEmail_NonExistingEmail_ReturnsFalse() {
        String email = "nonexisting@example.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean result = userRepository.existsByEmail(email);

        Assertions.assertFalse(result);
    }
}
