package com.staybits.gigmapapi.relatedevents.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.AddParticipantCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.CreateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.DeleteRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.RemoveParticipantCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.UpdateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.services.RelatedEventCommandService;
import com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories.RelatedEventRepository;
import org.springframework.stereotype.Service;

@Service
public class RelatedEventCommandServiceImpl implements RelatedEventCommandService {

    private final RelatedEventRepository relatedEventRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;

    public RelatedEventCommandServiceImpl(RelatedEventRepository relatedEventRepository,
                                         ConcertRepository concertRepository,
                                         UserRepository userRepository,
                                         VenueRepository venueRepository) {
        this.relatedEventRepository = relatedEventRepository;
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public RelatedEvent handle(CreateRelatedEventCommand command) {
        var concert = concertRepository.findById(command.concertId())
                .orElseThrow(() -> new IllegalArgumentException("Concert with id %s not found".formatted(command.concertId())));

        var organizador = userRepository.findById(command.organizadorId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.organizadorId())));

        // Find Venue by name or create new one
        var venue = venueRepository.findByName(command.venue().getName())
                .orElseGet(() -> venueRepository.save(command.venue()));

        var relatedEvent = new RelatedEvent(
                concert,
                command.titulo(),
                command.datehour(),
                command.descripcion(),
                command.tipo(),
                venue,
                command.status(),
                organizador
        );

        return relatedEventRepository.save(relatedEvent);
    }

    @Override
    public RelatedEvent handle(UpdateRelatedEventCommand command) {
        var existingEvent = relatedEventRepository.findById(command.id());

        if (existingEvent.isEmpty()) {
            return null;
        }

        // Find Venue by name or create new one
        var venue = venueRepository.findByName(command.venue().getName())
                .orElseGet(() -> venueRepository.save(command.venue()));

        var relatedEvent = existingEvent.get();
        relatedEvent.setTitulo(command.titulo());
        relatedEvent.setDatehour(command.datehour());
        relatedEvent.setDescripcion(command.descripcion());
        relatedEvent.setVenue(venue);
        relatedEvent.setStatus(command.status());

        return relatedEventRepository.save(relatedEvent);
    }

    @Override
    public boolean handle(DeleteRelatedEventCommand command) {
        var relatedEvent = relatedEventRepository.findById(command.id());

        if (relatedEvent.isEmpty()) {
            return false;
        }

        relatedEventRepository.delete(relatedEvent.get());
        return true;
    }

    @Override
    public RelatedEvent handle(AddParticipantCommand command) {
        var relatedEvent = relatedEventRepository.findById(command.relatedEventId())
                .orElseThrow(() -> new IllegalArgumentException("RelatedEvent with id %s not found".formatted(command.relatedEventId())));

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.userId())));

        relatedEvent.getParticipantes().add(user);
        return relatedEventRepository.save(relatedEvent);
    }

    @Override
    public RelatedEvent handle(RemoveParticipantCommand command) {
        var relatedEvent = relatedEventRepository.findById(command.relatedEventId())
                .orElseThrow(() -> new IllegalArgumentException("RelatedEvent with id %s not found".formatted(command.relatedEventId())));

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.userId())));

        relatedEvent.getParticipantes().remove(user);
        return relatedEventRepository.save(relatedEvent);
    }
}
