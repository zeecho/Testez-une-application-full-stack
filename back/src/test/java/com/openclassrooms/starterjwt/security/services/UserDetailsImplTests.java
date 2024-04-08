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
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
    
    @Test
    @DisplayName("Test isAccountNonExpired")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test isAccountNonLocked")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test isEnabled")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Test equals - Same object")
    void testEquals_SameObject() {
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();

        assertTrue(userDetails.equals(otheruserDetails));
    }

    @Test
    @DisplayName("Test equals - Null object")
    void testEquals_NullObject() {
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();

        assertFalse(otheruserDetails.equals(null));
    }

    @Test
    @DisplayName("Test equals - Different class")
    void testEquals_DifferentClass() {
        UserDetailsImpl otheruserDetails = UserDetailsImpl.builder().id(1L).build();
        Object object = mock(Object.class);

        assertFalse(otheruserDetails.equals(object));
    }

    @Test
    @DisplayName("Test equals - Different ID")
    void testEquals_DifferentId() {
        UserDetailsImpl otheruserDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl otheruserDetails2 = UserDetailsImpl.builder().id(2L).build();

        assertFalse(otheruserDetails1.equals(otheruserDetails2));
    }
}
