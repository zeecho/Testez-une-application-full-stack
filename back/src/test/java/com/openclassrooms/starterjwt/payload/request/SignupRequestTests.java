package com.openclassrooms.starterjwt.payload.request;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTests {
    @Test
    @DisplayName("Check invalid fields")
    void testSignupRequestValidation() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid_email");
        signupRequest.setFirstName("A");
        signupRequest.setLastName("B");
        signupRequest.setPassword("123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(4);

        // Check violations messages
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "must be a well-formed email address",
                        "size must be between 3 and 20",
                        "size must be between 3 and 20",
                        "size must be between 6 and 40"
                );
    }

    @Test
    @DisplayName("Check valid fields")
    void testValidSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("strongPassword123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isEmpty();
    }
}
