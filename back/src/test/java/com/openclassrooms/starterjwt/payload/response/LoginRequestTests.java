package com.openclassrooms.starterjwt.payload.response;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTests {

    @Test
    @DisplayName("Check that empty fields do not pass")
    void testNotBlankValidation() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(2);
        assertThat(violations).allMatch(violation -> violation.getMessage().contains("must not be blank"));
    }

    @Test
    @DisplayName("Check that valid fields pass")
    void testValidLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).isEmpty();
    }
}
