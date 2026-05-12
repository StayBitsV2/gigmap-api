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
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;
import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;
import com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories.RelatedEventRepository;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.RelatedEventsController;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.CreateRelatedEventResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.RelatedEventResource;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.UpdateRelatedEventResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RelatedEventIntegrationTest {

    @Autowired
    private RelatedEventsController relatedEventsController;

    @Autowired
    private RelatedEventRepository relatedEventRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Test
    void testCreateRelatedEvent() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        Concert concert = concertRepository.save(new Concert("Concert", LocalDateTime.now().plusDays(10), "Desc", "Img",
                venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));

        VenueResource venueResource = new VenueResource("Venue " + uniqueSuffix, "Address", new BigDecimal("10.0"),
                new BigDecimal("20.0"), 100);
        CreateRelatedEventResource resource = new CreateRelatedEventResource(
                concert.getId(),
                "Pre-Party " + uniqueSuffix,
                LocalDateTime.now().plusDays(10).minusHours(2),
                "Description",
                "PREVIA",
                venueResource,
                "PUBLICADO",
                artist.getId());

        // Act
        ResponseEntity<RelatedEventResource> response = relatedEventsController.createRelatedEvent(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pre-Party " + uniqueSuffix, response.getBody().titulo());

        // Verify in DB
        Optional<RelatedEvent> event = relatedEventRepository.findById(response.getBody().id());
        assertTrue(event.isPresent());
        assertEquals("Pre-Party " + uniqueSuffix, event.get().getTitulo());
    }

    @Test
    void testGetRelatedEventById() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        Concert concert = concertRepository.save(new Concert("Concert", LocalDateTime.now().plusDays(10), "Desc", "Img",
                venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));
        RelatedEvent event = relatedEventRepository.save(new RelatedEvent(concert, "Event", LocalDateTime.now().plusDays(5), "Desc",
                EventType.PREVIA, venue, ConcertStatus.PUBLICADO, artist));

        // Act
        ResponseEntity<RelatedEventResource> response = relatedEventsController.getRelatedEventById(event.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(event.getId(), response.getBody().id());
    }

    @Test
    void testUpdateRelatedEvent() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User artist = userRepository.save(
                new User("artist-" + uniqueSuffix + "@example.com", "artist-" + uniqueSuffix, "Artist", Role.ARTIST));
        Venue venue = venueRepository.save(
                new Venue("Venue " + uniqueSuffix, new BigDecimal("10.0"), new BigDecimal("20.0"), "Address", 100));
        Platform platform = platformRepository.save(new Platform("Platform " + uniqueSuffix, "http://platform.img"));
        Concert concert = concertRepository.save(new Concert("Concert", LocalDateTime.now().plusDays(10), "Desc", "Img",
                venue, ConcertStatus.PUBLICADO, artist, Genre.ROCK, platform));
        RelatedEvent event = relatedEventRepository.save(new RelatedEvent(concert, "Old Title", LocalDateTime.now().plusDays(5),
                "Desc", EventType.PREVIA, venue, ConcertStatus.PUBLICADO, artist));

        VenueResource venueResource = new VenueResource("New Venue", "New Address", new BigDecimal("11.0"),
                new BigDecimal("21.0"), 200);
        UpdateRelatedEventResource updateResource = new UpdateRelatedEventResource(
                event.getId(),
                "New Title",
                LocalDateTime.now().plusHours(1),
                "New Desc",
                venueResource,
                "ENCURSO");

        // Act
        ResponseEntity<RelatedEventResource> response = relatedEventsController.updateRelatedEvent(event.getId(),
                updateResource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Title", response.getBody().titulo());

        // Verify in DB
        Optional<RelatedEvent> updatedEvent = relatedEventRepository.findById(event.getId());
        assertTrue(updatedEvent.isPresent());
        assertEquals("New Title", updatedEvent.get().getTitulo());
    }
}
