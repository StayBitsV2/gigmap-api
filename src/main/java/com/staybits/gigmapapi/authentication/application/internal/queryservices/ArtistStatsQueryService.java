package com.staybits.gigmapapi.authentication.application.internal.queryservices;

import com.staybits.gigmapapi.analytics.domain.model.valueobjects.AnalyticsEventType;
import com.staybits.gigmapapi.analytics.infrastructure.persistence.jpa.repositories.AnalyticsEventRepository;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.ArtistStatsResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.ExternalLinkClicksResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.WeeklyStatsResource;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class ArtistStatsQueryService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final UserRepository userRepository;

    public ArtistStatsQueryService(AnalyticsEventRepository analyticsEventRepository, UserRepository userRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
        this.userRepository = userRepository;
    }

    public ArtistStatsResource getArtistStats(Long artistId) {
        var artistOpt = userRepository.findById(artistId);
        if (artistOpt.isEmpty()) {
            throw new IllegalArgumentException("Artist not found with id: " + artistId);
        }

        var today = LocalDate.now();
        var formatter = DateTimeFormatter.ofPattern("dd MMM", new java.util.Locale("es"));
        var weeks = new ArrayList<WeeklyStatsResource>();
        boolean hasData = false;

        for (int i = 3; i >= 0; i--) {
            var weekStart = today.minusWeeks(i).with(DayOfWeek.MONDAY);
            var weekEnd = today.minusWeeks(i).with(DayOfWeek.SUNDAY);

            var startDateTime = weekStart.atStartOfDay();
            var endDateTime = weekEnd.atTime(LocalTime.MAX);

            var weekStartStr = weekStart.format(formatter);
            var weekEndStr = weekEnd.format(formatter);

            long newFollowers = analyticsEventRepository.countByUserIdAndEventTypeAndCreatedAtBetween(
                    artistId, AnalyticsEventType.ARTIST_FOLLOWED, startDateTime, endDateTime);

            long profileViews = analyticsEventRepository.countByUserIdAndEventTypeAndCreatedAtBetween(
                    artistId, AnalyticsEventType.PROFILE_VIEWED, startDateTime, endDateTime);

            var linkClickEvents = analyticsEventRepository.findByUserIdAndEventTypeAndCreatedAtBetween(
                    artistId, AnalyticsEventType.EXTERNAL_LINK_CLICKED, startDateTime, endDateTime);

            long spotifyClicks = 0;
            long instagramClicks = 0;
            long youtubeClicks = 0;

            for (var event : linkClickEvents) {
                var metadata = event.getMetadata();
                if (metadata != null) {
                    var lower = metadata.toLowerCase();
                    if (lower.contains("spotify"))
                        spotifyClicks++;
                    else if (lower.contains("instagram"))
                        instagramClicks++;
                    else if (lower.contains("youtube"))
                        youtubeClicks++;
                }
            }

            if (newFollowers > 0 || profileViews > 0 || spotifyClicks > 0 || instagramClicks > 0 || youtubeClicks > 0) {
                hasData = true;
            }

            weeks.add(new WeeklyStatsResource(
                    weekStartStr, weekEndStr,
                    newFollowers, profileViews,
                    new ExternalLinkClicksResource(spotifyClicks, instagramClicks, youtubeClicks)));
        }

        return new ArtistStatsResource(
                artistId,
                weeks,
                hasData,
                hasData ? null : "sin datos historicos");
    }
}
