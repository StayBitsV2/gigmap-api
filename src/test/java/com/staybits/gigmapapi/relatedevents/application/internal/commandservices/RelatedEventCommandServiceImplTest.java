package com.staybits.gigmapapi.relatedevents.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.AddParticipantCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.CreateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.DeleteRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.RemoveParticipantCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.UpdateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;
import com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories.RelatedEventRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatedEventCommandServiceImplTest {

    @Mock
    RelatedEventRepository relatedEventRepository;

    @Mock
    ConcertRepository concertRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    VenueRepository venueRepository;

    @InjectMocks
    RelatedEventCommandServiceImpl relatedEventCommandServiceImpl;

    BigDecimal latitud = new BigDecimal(100);
    BigDecimal longitud = new BigDecimal(100);

    Venue venue = new Venue("Estadio Nacional",latitud,longitud, "Lima",50);
    Platform platform = new Platform("YouTube", "img");

    User user = new User(
            "artist@test.com",
            "artist1",
            "Roberto",
            Role.ARTIST
    );

    Concert concert = new Concert(
            "Concierto Test",
            LocalDateTime.now(),
            "Descripcion",
            "img",
            venue,
            ConcertStatus.PUBLICADO,
            user,
            Genre.ROCK,
            platform
    );

    CreateRelatedEventCommand createCommand =
            new CreateRelatedEventCommand(
                    1L,
                    "Titulo evento",
                    LocalDateTime.now(),
                    "Descripcion evento",
                    EventType.AFTERPARTY,
                    venue,
                    ConcertStatus.PUBLICADO,
                    1L
            );

    @Test
    void Test_CreateRelatedEvent() {

        when(concertRepository.findById(1L))
                .thenReturn(Optional.of(concert));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(venueRepository.findByName("Estadio Nacional"))
                .thenReturn(Optional.of(venue));

        when(relatedEventRepository.save(any(RelatedEvent.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result = relatedEventCommandServiceImpl.handle(createCommand);

        assertNotNull(result);
        assertEquals("Titulo evento", result.getTitulo());
    }

    @Test
    void Test_UpdateRelatedEvent() {

        RelatedEvent relatedEvent = new RelatedEvent(
                concert,
                "Evento",
                LocalDateTime.now(),
                "Descripcion",
                EventType.AFTERPARTY,
                venue,
                ConcertStatus.PUBLICADO,
                user
        );

        UpdateRelatedEventCommand updateCommand =
                new UpdateRelatedEventCommand(
                        1L,
                        "Nuevo titulo",
                        LocalDateTime.now(),
                        "Nueva descripcion",
                        venue,
                        ConcertStatus.PUBLICADO
                );

        when(relatedEventRepository.findById(1L))
                .thenReturn(Optional.of(relatedEvent));

        when(venueRepository.findByName("Estadio Nacional"))
                .thenReturn(Optional.of(venue));

        when(relatedEventRepository.save(any(RelatedEvent.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result = relatedEventCommandServiceImpl.handle(updateCommand);

        assertNotNull(result);
        assertEquals("Nuevo titulo", result.getTitulo());
    }

    @Test
    void Test_DeleteRelatedEvent() {

        RelatedEvent relatedEvent = new RelatedEvent(
                concert,
                "Evento",
                LocalDateTime.now(),
                "Descripcion",
                EventType.AFTERPARTY,
                venue,
                ConcertStatus.PUBLICADO,
                user
        );

        DeleteRelatedEventCommand deleteCommand =
                new DeleteRelatedEventCommand(1L);

        when(relatedEventRepository.findById(1L))
                .thenReturn(Optional.of(relatedEvent));

        boolean result = relatedEventCommandServiceImpl.handle(deleteCommand);

        assertTrue(result);
    }

    @Test
    void Test_AddParticipant() {

        RelatedEvent relatedEvent = new RelatedEvent(
                concert,
                "Evento",
                LocalDateTime.now(),
                "Descripcion",
                EventType.AFTERPARTY,
                venue,
                ConcertStatus.PUBLICADO,
                user
        );

        User participante = new User(
                "fan@test.com",
                "fan1",
                "Alan",
                Role.FAN
        );

        AddParticipantCommand command =
                new AddParticipantCommand(1L, 1L);

        when(relatedEventRepository.findById(1L))
                .thenReturn(Optional.of(relatedEvent));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(participante));

        when(relatedEventRepository.save(any(RelatedEvent.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result = relatedEventCommandServiceImpl.handle(command);

        assertTrue(result.getParticipantes().contains(participante));
    }

    @Test
    void Test_RemoveParticipant() {

        RelatedEvent relatedEvent = new RelatedEvent(
                concert,
                "Evento",
                LocalDateTime.now(),
                "Descripcion",
                EventType.AFTERPARTY,
                venue,
                ConcertStatus.PUBLICADO,
                user
        );

        User participante = new User(
                "fan@test.com",
                "fan1",
                "Alan",
                Role.FAN
        );

        relatedEvent.getParticipantes().add(participante);

        RemoveParticipantCommand command =
                new RemoveParticipantCommand(1L, 1L);

        when(relatedEventRepository.findById(1L))
                .thenReturn(Optional.of(relatedEvent));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(participante));

        when(relatedEventRepository.save(any(RelatedEvent.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result = relatedEventCommandServiceImpl.handle(command);

        assertFalse(result.getParticipantes().contains(participante));
    }
}