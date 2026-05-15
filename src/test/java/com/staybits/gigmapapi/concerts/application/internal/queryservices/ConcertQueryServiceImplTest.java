package com.staybits.gigmapapi.concerts.application.internal.queryservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsAttendedByUserIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertByIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByArtistQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByGenreQuery;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;

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
class ConcertQueryServiceImplTest {

    @Mock
    ConcertRepository concertRepository;

    @InjectMocks
    ConcertQueryServiceImpl concertQueryServiceImpl;

    BigDecimal latitud = new BigDecimal(100);
    BigDecimal longitud = new BigDecimal(100);

    Venue venue = new Venue("Estadio Nacional",latitud,longitud, "Lima",5000);
    Platform platform = new Platform("YouTube", "img");

    User artist = new User(
            "artist@test.com",
            "artist1",
            "Roberto",
            Role.ARTIST
    );

    Concert concert = new Concert(
            "Concert Test",
            LocalDateTime.now(),
            "Descripcion",
            "img",
            venue,
            ConcertStatus.PUBLICADO,
            artist,
            Genre.ROCK,
            platform
    );

    @Test
    void Test_GetAllConcerts() {

        List<Concert> concerts = List.of(concert);

        when(concertRepository.findAll())
                .thenReturn(concerts);

        var result =
                concertQueryServiceImpl.handle(new GetAllConcertsQuery());

        assertEquals(1, result.size());
    }

    @Test
    void Test_GetConcertById() {

        when(concertRepository.findById(1L))
                .thenReturn(Optional.of(concert));

        var result =
                concertQueryServiceImpl.handle(
                        new GetConcertByIdQuery(1L)
                );

        assertTrue(result.isPresent());
        assertEquals("Concert Test", result.get().getTitle());
    }

    @Test
    void Test_GetConcertsByGenre() {

        List<Concert> concerts = List.of(concert);

        when(concertRepository.findByGenre(Genre.ROCK))
                .thenReturn(concerts);

        var result =
                concertQueryServiceImpl.handle(
                        new GetConcertsByGenreQuery(Genre.ROCK)
                );

        assertEquals(1, result.size());
    }

    @Test
    void Test_GetConcertsByArtist() {

        List<Concert> concerts = List.of(concert);

        when(concertRepository.findByUserId(1L))
                .thenReturn(concerts);

        var result =
                concertQueryServiceImpl.handle(
                        new GetConcertsByArtistQuery(1L)
                );

        assertEquals(1, result.size());
    }

    @Test
    void Test_GetConcertsAttendedByUserId() {

        List<Concert> concerts = List.of(concert);

        when(concertRepository.findByAttendees_Id(1L))
                .thenReturn(concerts);

        var result =
                concertQueryServiceImpl.handle(
                        new GetAllConcertsAttendedByUserIdQuery(1L)
                );

        assertEquals(1, result.size());
    }
}