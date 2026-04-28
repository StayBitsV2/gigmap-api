package com.staybits.gigmapapi.concerts.application.internal.queryservices;

import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsAttendedByUserIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertByIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByGenreQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByArtistQuery;
import com.staybits.gigmapapi.concerts.domain.services.ConcertQueryService;
import com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories.ConcertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Concert query service implementation.
 * <p>
 * This class implements the service to handle concert queries.
 * </p>
 */
@Service
public class ConcertQueryServiceImpl implements ConcertQueryService {

    private final ConcertRepository concertRepository;

    public ConcertQueryServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<Concert> handle(GetAllConcertsQuery query) {
        return concertRepository.findAll();
    }

    @Override
    public Optional<Concert> handle(GetConcertByIdQuery query) {
        return concertRepository.findById(query.id());
    }

    @Override
    public List<Concert> handle(GetConcertsByGenreQuery query) {
        return concertRepository.findByGenre(query.genre());
    }

    @Override
    public List<Concert> handle(GetConcertsByArtistQuery query) {
        return concertRepository.findByUserId(query.artistId());
    }

    @Override
    public List<Concert> handle(GetAllConcertsAttendedByUserIdQuery query) {
        return this.concertRepository.findByAttendees_Id(query.userId());
    }
}
