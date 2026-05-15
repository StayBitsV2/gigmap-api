package com.staybits.gigmapapi.concerts.domain.model.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VenueTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testVenueWithValidCapacity() {
        Venue venue = new Venue("Valid Venue", new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 10000);
        Set<ConstraintViolation<Venue>> violations = validator.validate(venue);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testVenueWithCapacityTooLow() {
        Venue venue = new Venue("Invalid Venue", new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 4999);
        Set<ConstraintViolation<Venue>> violations = validator.validate(venue);
        assertEquals(1, violations.size());
        assertEquals("Capacity must be at least 5000", violations.iterator().next().getMessage());
    }

    @Test
    void testVenueWithCapacityTooHigh() {
        Venue venue = new Venue("Invalid Venue", new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 80001);
        Set<ConstraintViolation<Venue>> violations = validator.validate(venue);
        assertEquals(1, violations.size());
        assertEquals("Capacity must be at most 80000", violations.iterator().next().getMessage());
    }
}
