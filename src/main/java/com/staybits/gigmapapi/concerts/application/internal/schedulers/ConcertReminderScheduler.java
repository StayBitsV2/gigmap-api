package com.staybits.gigmapapi.concerts.application.internal.schedulers;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.notifications.interfaces.acl.NotificationsContextFacade;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConcertReminderScheduler {

    private final ConcertRepository concertRepository;
    private final NotificationsContextFacade notificationsContextFacade;

    public ConcertReminderScheduler(ConcertRepository concertRepository,
            NotificationsContextFacade notificationsContextFacade) {
        this.concertRepository = concertRepository;
        this.notificationsContextFacade = notificationsContextFacade;
    }

    /**
     * Runs every hour at the top of the hour.
     * Finds concerts starting between 23 and 24 hours from now.
     */
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startWindow = now.plusHours(23);
        LocalDateTime endWindow = now.plusHours(24);

        List<Concert> upcomingConcerts = concertRepository.findByDatehourBetween(startWindow, endWindow);

        for (Concert concert : upcomingConcerts) {
            for (User attendee : concert.getAttendees()) {
                notificationsContextFacade.sendConcertReminder(
                        attendee.getId(),
                        concert.getId(),
                        concert.getTitle(),
                        concert.getVenue().getName(),
                        concert.getDatehour());
            }
        }
    }
}
