package com.openclassrooms.starterjwt.controller;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Authenticate user successfully")
	public void testAuthenticateUser() throws Exception {
        // Mocking the AuthenticationManager to return a fixed authentication object
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mocking the UserDetailsImpl to return a fixed UserDetails object
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe", true, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mocking the UserRepository to return a fixed User object
        User user = new User("test@example.com", "Doe", "John", "encodedPassword", true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Mocking the JwtUtils to return a fixed JWT token
        when(jwtUtils.generateJwtToken(any())).thenReturn("fakeJwtToken");

        // Perform the authentication
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertThat(jwtResponse.getToken()).isEqualTo("fakeJwtToken");
        assertThat(jwtResponse.getId()).isEqualTo(1L);
        assertThat(jwtResponse.getUsername()).isEqualTo("test@example.com");
        assertThat(jwtResponse.getFirstName()).isEqualTo("John");
        assertThat(jwtResponse.getLastName()).isEqualTo("Doe");
        assertThat(jwtResponse.getAdmin()).isTrue();
	}
    
    @Test
    public void testAuthenticateUserWithWrongCredentials() throws Exception {
        // Mocking the AuthenticationManager to throw BadCredentialsException
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Perform the authentication with wrong credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong_password");

        try {
            ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
            // If no exception is thrown, the test fails
            fail("Expected BadCredentialsException but it wasn't thrown");
        } catch (AuthenticationException e) {
            // Verify that the exception thrown is BadCredentialsException
            assertThat(e).isInstanceOf(BadCredentialsException.class);
        }
    }

    @Test
    @DisplayName("Register user successfully")
    public void testRegisterUserWhoDoesNotExist() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Doe");
        signupRequest.setLastName("John");
        signupRequest.setPassword("password");
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
    }
    
    @Test
    @DisplayName("Try to register a user already registered")
    public void testRegisterUserWhoAlreadyExists() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Doe");
        signupRequest.setLastName("John");
        signupRequest.setPassword("password");
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
