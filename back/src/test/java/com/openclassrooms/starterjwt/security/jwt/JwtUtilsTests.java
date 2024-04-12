package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class JwtUtilsTests {

    @Mock
    private Authentication authentication;

	@Mock
	private UserDetailsImpl userDetails;
    
    @InjectMocks
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Test token validation failure due to bad token")
    void testValidateJwtToken_BadToken() {
        String badToken = "badToken";

        boolean result = jwtUtils.validateJwtToken(badToken);

        assertThat(result).isFalse();
    }
}
