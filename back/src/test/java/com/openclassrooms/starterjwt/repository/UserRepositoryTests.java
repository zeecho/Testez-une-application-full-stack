package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {
    @Mock
    private UserRepository userRepository;
    
    @Test
    @DisplayName("Get an existing user using their email")
    public void testFindByEmail_ExistingEmail_ReturnsUser() {
        String email = "test@example.com";
        User user = new User(1L, email, "Doe", "John", "password", false, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        
        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(user.getEmail()).isEqualTo(result.get().getEmail());
    }
    

    @Test
    @DisplayName("Get a non existing user using their email (should return empty)")
    public void testFindByEmail_NonExistingEmail_ReturnsEmpty() {
        String email = "nonexisting@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Check if an email already exists (true)")
    public void testExistsByEmail_ExistingEmail_ReturnsTrue() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        
        boolean result = userRepository.existsByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check if an email already exists (false)")
    public void testExistsByEmail_NonExistingEmail_ReturnsFalse() {
        String email = "nonexisting@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        
        boolean result = userRepository.existsByEmail(email);

        assertThat(result).isFalse();
    }
}
