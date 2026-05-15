package com.staybits.gigmapapi.relatedevents.application.internal.queryservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventByIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventsByConcertIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;
import com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories.RelatedEventRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatedEventQueryServiceImplTest {

    @Mock
    RelatedEventRepository relatedEventRepository;

    @InjectMocks
    RelatedEventQueryServiceImpl relatedEventQueryServiceImpl;

    BigDecimal latitud = new BigDecimal(100);
    BigDecimal longitud = new BigDecimal(100);

    Venue venue = new Venue("Estadio Nacional",latitud,longitud, "Lima",5000);
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
    RelatedEvent relatedEvent = new RelatedEvent(
            concert,
            "Titulo evento",
            LocalDateTime.now(),
            "Descripcion evento",
            EventType.AFTERPARTY,
            venue,
            ConcertStatus.PUBLICADO,
            user
    );

    @Test
    void Test_GetRelatedEventById() {

        GetRelatedEventByIdQuery query =
                new GetRelatedEventByIdQuery(1L);

        when(relatedEventRepository.findById(1L))
                .thenReturn(Optional.of(relatedEvent));

        Optional<RelatedEvent> result =
                relatedEventQueryServiceImpl.handle(query);

        assertTrue(result.isPresent());
        assertEquals("Titulo evento", result.get().getTitulo());
    }

    @Test
    void Test_GetRelatedEventsByConcertId() {

        GetRelatedEventsByConcertIdQuery query =
                new GetRelatedEventsByConcertIdQuery(1L);

        List<RelatedEvent> events = List.of(relatedEvent);

        when(relatedEventRepository.findByConcertId(1L))
                .thenReturn(events);

        List<RelatedEvent> result =
                relatedEventQueryServiceImpl.handle(query);

        assertEquals(1, result.size());
        assertEquals("Titulo evento", result.get(0).getTitulo());
    }
}