package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VenueResourceTest {

    @Test
    void testVenueResourceWithValidData() {
        assertDoesNotThrow(() -> new VenueResource("Valid Venue", "Address", new BigDecimal("10.0"), new BigDecimal("20.0"), 10000));
    }

    @Test
    void testVenueResourceWithCapacityTooLow() {
        assertThrows(IllegalArgumentException.class, () -> 
            new VenueResource("Invalid Venue", "Address", new BigDecimal("10.0"), new BigDecimal("20.0"), 4999));
    }

    @Test
    void testVenueResourceWithCapacityTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> 
            new VenueResource("Invalid Venue", "Address", new BigDecimal("10.0"), new BigDecimal("20.0"), 80001));
    }

    @Test
    void testVenueResourceWithInvalidLatitude() {
        assertThrows(IllegalArgumentException.class, () -> 
            new VenueResource("Invalid Venue", "Address", new BigDecimal("91.0"), new BigDecimal("20.0"), 10000));
    }

    @Test
    void testVenueResourceWithInvalidLongitude() {
        assertThrows(IllegalArgumentException.class, () -> 
            new VenueResource("Invalid Venue", "Address", new BigDecimal("10.0"), new BigDecimal("181.0"), 10000));
    }
}
