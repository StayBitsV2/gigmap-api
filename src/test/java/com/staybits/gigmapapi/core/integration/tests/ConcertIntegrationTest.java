package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.PlatformRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.staybits.gigmapapi.concerts.interfaces.rest.ConcertsController;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.ConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.CreateConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.PlatformResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.UpdateConcertResource;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ConcertIntegrationTest {

    @Autowired
    private ConcertsController concertsController;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Test
    void testCreateConcert() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));

        VenueResource venueResource = new VenueResource("Venue " + uniqueSuffix, "Address", new BigDecimal("10.0"),
                new BigDecimal("20.0"), 100);
        PlatformResource platformResource = new PlatformResource("Platform " + uniqueSuffix, "http://platform.img");
        CreateConcertResource resource = new CreateConcertResource(
                "Concert " + uniqueSuffix,
                "Description",
                "http://concert.img",
                LocalDateTime.now().plusDays(10),
                venueResource,
                "ROCK",
                "PUBLICADO",
                platformResource,
                artist.getId());

        // Act
        ResponseEntity<ConcertResource> response = concertsController.createConcert(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Concert " + uniqueSuffix, response.getBody().name());

        // Verify in DB
        Optional<Concert> concert = concertRepository.findById(response.getBody().id());
        assertTrue(concert.isPresent());
        assertEquals("Concert " + uniqueSuffix, concert.get().getTitle());
        assertEquals(Role.ARTIST, concert.get().getUser().getRole());
    }

    @Test
    void testGetAllConcerts() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        concertRepository.save(new Concert("Concert " + uniqueSuffix, LocalDateTime.now().plusDays(10), "Desc", "Img",
                venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));

        // Act
        ResponseEntity<List<ConcertResource>> response = concertsController.getAllConcerts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }

    @Test
    void testGetConcertById() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        Concert concert = concertRepository
                .save(new Concert("Concert " + uniqueSuffix, LocalDateTime.now().plusDays(10), "Desc", "Img", venue,
                        ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));

        // Act
        ResponseEntity<ConcertResource> response = concertsController.getConcertById(concert.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(concert.getId(), response.getBody().id());
    }

    @Test
    void testUpdateConcert() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        Concert concert = concertRepository.save(new Concert("Old Concert", LocalDateTime.now().plusDays(10), "Desc",
                "Img", venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));

        VenueResource venueResource = new VenueResource("New Venue", "New Address", new BigDecimal("11.0"),
                new BigDecimal("21.0"), 200);
        UpdateConcertResource updateResource = new UpdateConcertResource(
                concert.getId(),
                "New Concert Title",
                "New Description",
                "http://new.img",
                LocalDateTime.now().plusDays(15),
                venueResource,
                "ENCURSO");

        // Act
        ResponseEntity<ConcertResource> response = concertsController.updateConcert(concert.getId(), updateResource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Concert Title", response.getBody().name());

        // Verify in DB
        Optional<Concert> updatedConcert = concertRepository.findById(concert.getId());
        assertTrue(updatedConcert.isPresent());
        assertEquals("New Concert Title", updatedConcert.get().getTitle());
        assertEquals(ConcertStatus.ENCURSO, updatedConcert.get().getStatus());
    }

    @Test
    void testCreateConcert_WhenVenueAndTimeConflict_ShouldReturnBadRequest() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));

        LocalDateTime concertTime = LocalDateTime.now().plusDays(20);
        Venue venue = venueRepository.save(
                new Venue("Shared Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        
        // Create first concert
        concertRepository.save(new Concert("First Concert", concertTime, "Desc", "Img",
                venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));

        VenueResource venueResource = new VenueResource("Shared Venue " + uniqueSuffix, "Address", new BigDecimal("10.0"),
                new BigDecimal("20.0"), 100);
        PlatformResource platformResource = new PlatformResource("Platform " + uniqueSuffix, "http://platform.img");
        CreateConcertResource resource = new CreateConcertResource(
                "Second Concert",
                "Description",
                "http://concert.img",
                concertTime,
                venueResource,
                "ROCK",
                "PUBLICADO",
                platformResource,
                artist.getId());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> concertsController.createConcert(resource));
    }

    @Test
    void testCreateConcertWithPastDate() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));

        VenueResource venueResource = new VenueResource("Venue " + uniqueSuffix, "Address", new BigDecimal("10.0"),
                new BigDecimal("20.0"), 100);
        PlatformResource platformResource = new PlatformResource("Platform " + uniqueSuffix, "http://platform.img");
        
        // Use a past date
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CreateConcertResource(
                "Concert " + uniqueSuffix,
                "Description",
                "http://concert.img",
                pastDate,
                venueResource,
                "ROCK",
                "PUBLICADO",
                platformResource,
                artist.getId());
        });
    }
}
