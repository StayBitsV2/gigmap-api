package com.staybits.gigmapapi.concerts.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.commands.AddAttendeeCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.CreateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.DeleteConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.RemoveAttendeeCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.UpdateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.PlatformRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
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
class ConcertCommandServiceImplTest {

        @Mock
        ConcertRepository concertRepository;

        @Mock
        UserRepository userRepository;

        @Mock
        VenueRepository venueRepository;

        @Mock
        PlatformRepository platformRepository;

        @InjectMocks
        ConcertCommandServiceImpl concertCommandServiceImpl;
        BigDecimal latitud = new BigDecimal(100);
        BigDecimal longitud = new BigDecimal(100);

        Venue venue = new Venue("Estadio Nacional", latitud, longitud, "Lima", 5000);
        Platform platform = new Platform("YouTube", "img");

        User artist = new User(
                        "artist@test.com",
                        "artist1",
                        "Roberto",
                        Role.ARTIST);

        User fan = new User(
                        "fan@test.com",
                        "fan1",
                        "Alan",
                        Role.FAN);

        @Test
        void Test_CreateConcert() {

                CreateConcertCommand command = new CreateConcertCommand(
                                "Concierto Test",
                                LocalDateTime.now(),
                                "Descripcion",
                                "img",
                                venue,
                                ConcertStatus.PUBLICADO,
                                1L,
                                Genre.ROCK,
                                platform);

                when(userRepository.findById(1L))
                                .thenReturn(Optional.of(artist));

                when(venueRepository.findByName("Estadio Nacional"))
                                .thenReturn(Optional.of(venue));

                when(platformRepository.findByName("YouTube"))
                                .thenReturn(Optional.of(platform));

                when(concertRepository.save(any(Concert.class)))
                                .thenAnswer(i -> i.getArgument(0));

                var result = concertCommandServiceImpl.handle(command);

                assertEquals("Concierto Test", result.getTitle());
        }

        @Test
        void Test_CreateConcert_WhenVenueAndTimeConflict_ShouldThrowException() {
                LocalDateTime now = LocalDateTime.now();
                CreateConcertCommand command = new CreateConcertCommand(
                                "Concierto Conflictivo",
                                now,
                                "Descripcion",
                                "img",
                                venue,
                                ConcertStatus.PUBLICADO,
                                1L,
                                Genre.ROCK,
                                platform);

                when(userRepository.findById(1L))
                                .thenReturn(Optional.of(artist));

                when(venueRepository.findByName("Estadio Nacional"))
                                .thenReturn(Optional.of(venue));

                when(concertRepository.existsByDatehourAndVenue(now, venue))
                                .thenReturn(true);

                assertThrows(IllegalArgumentException.class, () -> concertCommandServiceImpl.handle(command));
        }

        @Test
        void Test_UpdateConcert() {

                Concert concert = new Concert(
                                "Old Concert",
                                LocalDateTime.now(),
                                "Old Desc",
                                "img",
                                venue,
                                ConcertStatus.PUBLICADO,
                                artist,
                                Genre.ROCK,
                                platform);

                UpdateConcertCommand command = new UpdateConcertCommand(
                                1L,
                                "New Concert",
                                LocalDateTime.now(),
                                "New Desc",
                                "newimg",
                                venue,
                                ConcertStatus.CANCELADO);

                when(concertRepository.findById(1L))
                                .thenReturn(Optional.of(concert));

                when(venueRepository.findByName("Estadio Nacional"))
                                .thenReturn(Optional.of(venue));

                when(concertRepository.save(any(Concert.class)))
                                .thenAnswer(i -> i.getArgument(0));

                var result = concertCommandServiceImpl.handle(command);

                assertEquals("New Concert", result.getTitle());
        }

        @Test
        void Test_UpdateConcert_WhenVenueAndTimeConflict_ShouldThrowException() {
                LocalDateTime now = LocalDateTime.now();
                Concert concert = new Concert(
                                "Old Concert",
                                now,
                                "Old Desc",
                                "img",
                                venue,
                                ConcertStatus.PUBLICADO,
                                artist,
                                Genre.ROCK,
                                platform);

                UpdateConcertCommand command = new UpdateConcertCommand(
                                1L,
                                "New Concert",
                                now,
                                "New Desc",
                                "newimg",
                                venue,
                                ConcertStatus.CANCELADO);

                when(concertRepository.findById(1L))
                                .thenReturn(Optional.of(concert));

                when(venueRepository.findByName("Estadio Nacional"))
                                .thenReturn(Optional.of(venue));

                when(concertRepository.existsByDatehourAndVenueAndIdNot(now, venue, 1L))
                                .thenReturn(true);

                assertThrows(IllegalArgumentException.class, () -> concertCommandServiceImpl.handle(command));
        }

        @Test
        void Test_DeleteConcert() {

                Concert concert = new Concert();

                DeleteConcertCommand command = new DeleteConcertCommand(1L);

                when(concertRepository.findById(1L))
                                .thenReturn(Optional.of(concert));

                boolean result = concertCommandServiceImpl.handle(command);

                assertTrue(result);
        }

        @Test
        void Test_AddAttendee() {

                Concert concert = new Concert();

                AddAttendeeCommand command = new AddAttendeeCommand(1L, 2L);

                when(concertRepository.findById(1L))
                                .thenReturn(Optional.of(concert));

                when(userRepository.findById(2L))
                                .thenReturn(Optional.of(fan));

                when(concertRepository.save(any(Concert.class)))
                                .thenAnswer(i -> i.getArgument(0));

                var result = concertCommandServiceImpl.handle(command);

                assertTrue(result.getAttendees().contains(fan));
        }

        @Test
        void Test_RemoveAttendee() {

                Concert concert = new Concert();

                concert.getAttendees().add(fan);

                RemoveAttendeeCommand command = new RemoveAttendeeCommand(1L, 2L);

                when(concertRepository.findById(1L))
                                .thenReturn(Optional.of(concert));

                when(userRepository.findById(2L))
                                .thenReturn(Optional.of(fan));

                when(concertRepository.save(any(Concert.class)))
                                .thenAnswer(i -> i.getArgument(0));

                var result = concertCommandServiceImpl.handle(command);

                assertFalse(result.getAttendees().contains(fan));
        }
}