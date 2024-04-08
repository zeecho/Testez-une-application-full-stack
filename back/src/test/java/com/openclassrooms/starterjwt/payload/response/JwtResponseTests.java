package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtResponseTests {
    @Test
    void testTokenSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setToken("newAccessToken");
        assertEquals("newAccessToken", jwtResponse.getToken());
    }

    @Test
    void testTypeSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setType("newType");
        assertEquals("newType", jwtResponse.getType());
    }

    @Test
    void testIdSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setId(2L);
        assertEquals(2L, jwtResponse.getId());
    }

    @Test
    void testUsernameSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setUsername("newUsername");
        assertEquals("newUsername", jwtResponse.getUsername());
    }

    @Test
    void testFirstNameSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setFirstName("newFirstName");
        assertEquals("newFirstName", jwtResponse.getFirstName());
    }

    @Test
    void testLastNameSetter() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "username", "firstName", "lastName", true);
        jwtResponse.setLastName("newLastName");
        assertEquals("newLastName", jwtResponse.getLastName());
    }
}
