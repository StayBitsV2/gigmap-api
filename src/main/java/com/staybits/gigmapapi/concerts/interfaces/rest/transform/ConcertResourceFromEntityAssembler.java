package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.ConcertResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConcertResourceFromEntityAssembler {

    private static List<Long> toListFromHashSet(Set<User> attendees) {
        if (attendees == null) return List.of();
        return attendees.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public static ConcertResource toResourceFromEntity(Concert entity) {
        if (entity == null) return null;
        
        return new ConcertResource(
            entity.getId(),
            entity.getTitle(),
            entity.getDatehour(),
            entity.getStatus() != null ? entity.getStatus().name() : null,
            entity.getDescription(),
            entity.getImageUrl(),
            entity.getGenre() != null ? entity.getGenre().name() : null,
            PlatformAssembler.toResourceFromEntity(entity.getPlatform()),
            VenueAssembler.toResourceFromEntity(entity.getVenue()),
            toListFromHashSet(entity.getAttendees())
        );
    }

    public static List<ConcertResource> toResourcesFromEntities(List<Concert> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(ConcertResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
