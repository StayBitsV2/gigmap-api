package com.staybits.gigmapapi.concerts.application.internal.schedulers;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.notifications.interfaces.acl.NotificationsContextFacade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertReminderSchedulerTest {

    @Mock
    ConcertRepository concertRepository;

    @Mock
    NotificationsContextFacade notificationsContextFacade;

    @InjectMocks
    ConcertReminderScheduler concertReminderScheduler;

    @Test
    void checkAndSendReminders() {
        BigDecimal latitud = new BigDecimal(100);
        BigDecimal longitud = new BigDecimal(100);

        Venue venue = new Venue("Estadio Nacional",latitud,longitud, "Lima",50);
        Platform platform = new Platform("YouTube", "img");
        User artist = new User(
                "artist@test.com",
                "artist1",
                "Roberto",
                Role.ARTIST
        );

        User attendee = new User(
                "fan@test.com",
                "fan1",
                "Alan",
                Role.FAN
        );

        Concert concert = new Concert(
                "Concert Test",
                LocalDateTime.now().plusHours(23),
                "Descripcion",
                "img",
                venue,
                ConcertStatus.PUBLICADO,
                artist,
                Genre.ROCK,
                platform
        );

        concert.getAttendees().add(attendee);

        when(concertRepository.findByDatehourBetween(any(), any()))
                .thenReturn(List.of(concert));

        concertReminderScheduler.checkAndSendReminders();

        verify(notificationsContextFacade, times(1))
                .sendConcertReminder(
                        any(),
                        any(),
                        eq("Concert Test"),
                        eq("Estadio Nacional"),
                        any()
                );
    }
}