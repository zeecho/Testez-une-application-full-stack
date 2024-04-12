package com.openclassrooms.starterjwt.payload.request;

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
    

    @Test
    @DisplayName("Test getter and setter methods")
    void testGetterAndSetterMethods() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("strongPassword123");

        // Test getter methods
        assertThat(signupRequest.getEmail()).isEqualTo("test@example.com");
        assertThat(signupRequest.getFirstName()).isEqualTo("John");
        assertThat(signupRequest.getLastName()).isEqualTo("Doe");
        assertThat(signupRequest.getPassword()).isEqualTo("strongPassword123");

        // Test setter methods
        signupRequest.setEmail("newemail@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Smith");
        signupRequest.setPassword("newStrongPassword123");

        assertThat(signupRequest.getEmail()).isEqualTo("newemail@example.com");
        assertThat(signupRequest.getFirstName()).isEqualTo("Jane");
        assertThat(signupRequest.getLastName()).isEqualTo("Smith");
        assertThat(signupRequest.getPassword()).isEqualTo("newStrongPassword123");
    }
    

    @Test
    @DisplayName("Test equals()")
    void testEquals() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("different@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("password");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
    }

    @Test
    @DisplayName("Test hashCode()")
    void testHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("different@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("password");

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }

    @Test
    @DisplayName("Test toString()")
    void testToString() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        String expectedToString = "SignupRequest(email=test@example.com, firstName=John, lastName=Doe, password=password)";

        assertThat(request.toString()).isEqualTo(expectedToString);
    }
}
