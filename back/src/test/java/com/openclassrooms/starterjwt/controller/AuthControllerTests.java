package com.openclassrooms.starterjwt.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTests {
    private MockMvc mvc;
    
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
    
    private JacksonTester<User> jsonUser;
    
    @BeforeEach
    void init() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
    }

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
	
	    // Perform the HTTP POST request
	    MockHttpServletResponse response = mvc.perform(
	            post("/api/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
	            .andReturn().getResponse();
	
	    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	    // Verify that the AuthenticationManager was called with the correct parameters
	    verify(authenticationManager).authenticate(
	            argThat(auth -> {
	                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
	                return token.getName().equals("test@example.com") && token.getCredentials().equals("password");
	            }));
	
	    // Verify that the UserRepository was called to find the user by email
	    verify(userRepository).findByEmail("test@example.com");
	
	    // Verify that the JwtUtils was called to generate JWT token
	    verify(jwtUtils).generateJwtToken(authentication);
	
	    // Assert the response content contains the expected JWT token
	    assertThat(response.getContentAsString()).isEqualTo("{\"token\":\"fakeJwtToken\",\"type\":\"Bearer\",\"id\":1,\"username\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"admin\":true}");
	}
    
//    TODO This doesn't work
//    @Test
//    public void testAuthenticateUserWithWrongCredentials() throws Exception {
//        // Mocking the AuthenticationManager to throw BadCredentialsException
//        when(authenticationManager.authenticate(any()))
//                .thenThrow(new BadCredentialsException("Bad credentials"));
//
//        // Perform the HTTP POST request
//        MockHttpServletResponse response = mvc.perform(
//                post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\",\"password\":\"wrongPassword\"}"))
//                .andReturn().getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
////        assertThat(response.getContentAsString()).isEmpty();
//    }

    @Test
    @DisplayName("Register user successfully")
    public void testRegisterUserWhoDoesNotExist() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User("test@example.com", "Doe", "John", "password", false))
                        .getJson()
                )).andReturn().getResponse();
        
        verify(userRepository).save(argThat(user ->
			        user.getEmail().equals("test@example.com") &&
			        user.getLastName().equals("Doe") &&
			        user.getFirstName().equals("John") &&
			        user.getPassword().equals("encodedPassword"))
        		);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"User registered successfully!\"}");
    }
    
    @Test
    @DisplayName("Try to regiter a user already registered")
    public void testRegisterUserWhoAlreadyExists() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User("test@example.com", "Doe", "John", "password", false))
                        .getJson()
                )).andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"Error: Email is already taken!\"}");
    }

    @Test
    @DisplayName("Try to register a user with an email too long")
    public void testRegisterUserWithEmailTooLong() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User(
                        		"testtesttesttesttesttesttesttesttesttesttesttesttest@example.com", 
                        		"Doe", 
                        		"John", 
                        		"password", 
                        		false))
                        .getJson()
                )).andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
    
    @Test
    @DisplayName("Try to register a user with a lastname too long")
    public void testRegisterUserWithLastNameTooLong() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User(
                        		"test@example.com", 
                        		"DoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoe", 
                        		"John", 
                        		"password", 
                        		false))
                        .getJson()
                )).andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("Try to register a user without a lastname")
    public void testRegisterUserWithNoLastName() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User(
                        		"test@example.com", 
                        		"", 
                        		"John", 
                        		"password", 
                        		false))
                        .getJson()
                )).andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
    
    @Test
    @DisplayName("Try to register a user without an email")
    public void testRegisterUserWithNoEmail() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        jsonUser
                        .write(new User(
                        		"", 
                        		"Doe", 
                        		"John", 
                        		"password", 
                        		false))
                        .getJson()
                )).andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
}
