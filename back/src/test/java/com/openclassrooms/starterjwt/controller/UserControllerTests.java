package com.openclassrooms.starterjwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {
    @Mock
    private UserService userService;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserController userController;
    
    private User user;
    
    @BeforeEach
    void init() {
        this.user = new User(1L, "test@example.com", "John", "Doe", "encodedPassword", false, null, null);
    }
    
    @Test
    @DisplayName("Get a specific user successfully")
    public void testFindById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(user);
        UserDto userDto = new UserDto();
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    
    @Test
    @DisplayName("Get a specific user (not found)")
    public void testFindById_NotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
    
    @Test
    @DisplayName("Get a specific user (invalid)")
    void testFindById_InvalidId() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
    
    @Test
    @DisplayName("Delete user with success")
    void testSave_Success() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    	when(userService.findById(anyLong())).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Try to delete user (not found)")
    void testSave_NotFound() {
        when(userService.findById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Try to delete user with invalid id")
    void testSave_InvalidId() {
        ResponseEntity<?> response = userController.save("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
    
    @Test
    @DisplayName("Save user unauthorized")
    void testSave_Unauthorized() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test2@example.com");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    	when(userService.findById(anyLong())).thenReturn(user);
    	
        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
    }
}
