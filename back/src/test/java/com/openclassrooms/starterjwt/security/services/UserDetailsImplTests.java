package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTests {
    @Mock
    private UserDetailsImpl userDetails;
    
    @BeforeEach
    public void init() {
    	userDetails = UserDetailsImpl.builder().id(1L).username("test@example.com").firstName("John").lastName("Doe").admin(true).password("password").build();
    }
    
    @Test
    @DisplayName("Test getAuthorities")
    void testGetAuthorities() {
        // Given
//        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // When
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
    
    @Test
    @DisplayName("Test isAccountNonExpired")
    void testIsAccountNonExpired() {
        // Given
//        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Then
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test isAccountNonLocked")
    void testIsAccountNonLocked() {
        // Given
//        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Then
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired")
    void testIsCredentialsNonExpired() {
        // Given
//        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Then
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test isEnabled")
    void testIsEnabled() {
        // Given
//        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Then
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Test equals - Same object")
    void testEquals_SameObject() {
        // Given
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();

        // Then
        assertTrue(userDetails.equals(otheruserDetails));
    }

    @Test
    @DisplayName("Test equals - Null object")
    void testEquals_NullObject() {
        // Given
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();

        // Then
        assertFalse(otheruserDetails.equals(null));
    }

    @Test
    @DisplayName("Test equals - Different class")
    void testEquals_DifferentClass() {
        // Given
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();
        Object object = mock(Object.class);

        // Then
        assertFalse(otheruserDetails.equals(object));
    }

    @Test
    @DisplayName("Test equals - Different ID")
    void testEquals_DifferentId() {
        // Given
        UserDetailsImpl otheruserDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl otheruserDetails2 = UserDetailsImpl.builder().id(2L).build();

        // Then
        assertFalse(otheruserDetails1.equals(otheruserDetails2));
    }
}
