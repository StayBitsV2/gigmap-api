package com.staybits.gigmapapi.concerts.application.internal.commandservices;
  
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.commands.AddAttendeeCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.CreateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.DeleteConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.RemoveAttendeeCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.UpdateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.services.ConcertCommandService;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.PlatformRepository;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.VenueRepository;
import org.springframework.stereotype.Service;

/**
 * Concert command service implementation.
 * <p>
 *     This class implements the service to handle concert commands.
 * </p>
 */
@Service
public class ConcertCommandServiceImpl implements ConcertCommandService {

    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final PlatformRepository platformRepository;

    public ConcertCommandServiceImpl(ConcertRepository concertRepository, UserRepository userRepository, 
                                     VenueRepository venueRepository, PlatformRepository platformRepository) {
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
        this.platformRepository = platformRepository;
    }

    @Override
    public Concert handle(CreateConcertCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.userId())));

        // Find Venue by name or create new one
        var venue = venueRepository.findByName(command.venue().getName())
                .orElseGet(() -> venueRepository.save(command.venue()));

        // Find Platform by name or create new one
        var platform = platformRepository.findByName(command.platform().getName())
                .orElseGet(() -> platformRepository.save(command.platform()));

        var concert = new Concert(
                command.title(),
                command.datehour(),
                command.description(),
                command.imageUrl(),
                venue,
                command.status(),
                user,
                command.genre(),
                platform
        );

        // Validate that the user is an artist
        if (!concert.isValidArtist()) {
            throw new IllegalArgumentException("User must have ARTIST role to create a concert");
        }

        return concertRepository.save(concert);
    }

    @Override
    public Concert handle(UpdateConcertCommand command) {
        var existingConcert = concertRepository.findById(command.id());
        
        if (existingConcert.isEmpty()) {
            return null;
        }

        // Find Venue by name or create new one
        var venue = venueRepository.findByName(command.venue().getName())
                .orElseGet(() -> venueRepository.save(command.venue()));

        var concert = existingConcert.get();
        concert.setTitle(command.title());
        concert.setDatehour(command.datehour());
        concert.setDescription(command.description());
        concert.setImageUrl(command.imageUrl());
        concert.setVenue(venue);
        concert.setStatus(command.status());

        return concertRepository.save(concert);
    }

    @Override
    public boolean handle(DeleteConcertCommand command) {
        var concert = concertRepository.findById(command.id());
        
        if (concert.isEmpty()) {
            return false;
        }

        concertRepository.delete(concert.get());
        return true;
    }

    @Override
    public Concert handle(AddAttendeeCommand command) {
        var concert = concertRepository.findById(command.concertId())
                .orElseThrow(() -> new IllegalArgumentException("Concert with id %s not found".formatted(command.concertId())));
        
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.userId())));
        
        concert.getAttendees().add(user);
        return concertRepository.save(concert);
    }

    @Override
    public Concert handle(RemoveAttendeeCommand command) {
        var concert = concertRepository.findById(command.concertId())
                .orElseThrow(() -> new IllegalArgumentException("Concert with id %s not found".formatted(command.concertId())));
        
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %s not found".formatted(command.userId())));
        
        concert.getAttendees().remove(user);
        return concertRepository.save(concert);
    }
}
