package com.staybits.gigmapapi;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConcertTests {

    private Concert concert;
    private User artist;
    private Venue venue;
    private Platform platform;

    @BeforeEach
    void setUp() {
        artist = new User();
        artist.setId(1L);
        artist.setRole(Role.ARTIST);

        venue = new Venue(
                "Estadio San Marcos",
                new BigDecimal("-12.05600000"),
                new BigDecimal("-77.08400000"),
                "Av. Venezuela s/n",
                50000
        );

        platform = new Platform("Ticketmaster", "https://image.com/tm.png");

        concert = new Concert(
                "Concierto Coldplay",
                LocalDateTime.of(2025, 11, 29, 20, 0),
                "Gira mundial",
                "https://image.com/concert.jpg",
                venue,
                ConcertStatus.PUBLICADO,
                artist,
                Genre.ROCK,
                platform
        );
    }

    @Test
    void constructor_ShouldSetBasicFields() {
        assertThat(concert.getTitle()).isEqualTo("Concierto Coldplay");
        assertThat(concert.getDescription()).isEqualTo("Gira mundial");
        assertThat(concert.getVenue().getName()).isEqualTo("Estadio San Marcos");
        assertThat(concert.getGenre()).isEqualTo(Genre.ROCK);
        assertThat(concert.getStatus()).isEqualTo(ConcertStatus.PUBLICADO);
    }

    @Test
    void isValidArtist_ShouldReturnTrue_WhenUserHasArtistRole() {
        assertThat(concert.isValidArtist()).isTrue();
    }

    @Test
    void isValidArtist_ShouldReturnFalse_WhenUserIsNotArtist() {
        User normalUser = new User();
        normalUser.setId(2L);
        normalUser.setRole(Role.ARTIST);

        concert.setUser(normalUser);

        assertThat(concert.isValidArtist()).isFalse();
    }
}