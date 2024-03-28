package com.openclassrooms.starterjwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {
    private MockMvc mvc;

    @Mock
    private UserService userService;

    private UserMapper userMapper = new UserMapperImpl();
    
    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(new UserController(userService, userMapper)).build();
    }
    
    @Test
    @DisplayName("Get a specific user successfully")
    public void testFindById() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);
        user.setPassword("encodedPassword");
        when(userService.findById(1L)).thenReturn(user);

        MockHttpServletResponse response = mvc.perform(get("/api/user/1")).andReturn().getResponse();

        verify(userService).findById(1L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        
        UserDto expectedDto = new UserDto(user.getId(), user.getEmail(), user.getLastName(), user.getFirstName(), user.isAdmin(), null, user.getCreatedAt(), user.getUpdatedAt());
        String expectedJson = new ObjectMapper().writeValueAsString(expectedDto);
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
    
    @Test
    @DisplayName("Get a specific user (not found)")
    public void testFindById_NotFound() throws Exception {
        // Arrange
        when(userService.findById(1L)).thenReturn(null);
        // Act & Assert
        assertThat(mvc.perform(get("/api/user/1")).andReturn().getResponse().getStatus())
                .isEqualTo(404);
    }

    @Test
    @DisplayName("Delete a user successfully")
    public void testDeleteUserWithValidIdSuccess() throws Exception {
        // Create the user we want to delete 
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);
        when(userService.findById(1L)).thenReturn(user);
        
        // Authenticate with a second user who will try to delete the first one
        UserDetails userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MockHttpServletResponse response = mvc.perform(delete("/api/user/1"))
								        		.andReturn()
								        		.getResponse();
        
        verify(userService).delete(1L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("Delete a user unsuccessfully")
    public void testDeleteUserWithInvalidId() throws Exception {
        // Arrange
        when(userService.findById(1L)).thenReturn(null);

        // Act & Assert
        verify(userService, never()).delete(1L);
        assertThat(mvc.perform(delete("/api/user/1")).andReturn().getResponse().getStatus())
                .isEqualTo(404);
    }

    @Test
    @DisplayName("Try to delete a user (unauthorized)")
    public void testDeleteUserUnauthorized() throws Exception {
        // Create the user we want to delete 
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);
        when(userService.findById(1L)).thenReturn(user);
        
        // Authenticate with a second user who will try to delete the first one
        UserDetails userDetails = new UserDetailsImpl(2L, "test2@example.com", "John", "Doe", true, "encodedPassword");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        MockHttpServletResponse response = mvc.perform(
        		delete("/api/user/1")
        		)
        		.andReturn()
        		.getResponse();
        
        verify(userService, never()).delete(1L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
}
