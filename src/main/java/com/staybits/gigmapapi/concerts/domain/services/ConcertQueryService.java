package com.staybits.gigmapapi.concerts.domain.services;

import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsAttendedByUserIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetAllConcertsQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertByIdQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByGenreQuery;
import com.staybits.gigmapapi.concerts.domain.model.queries.GetConcertsByArtistQuery;

import java.util.List;
import java.util.Optional;

public interface ConcertQueryService {
  List<Concert> handle(GetAllConcertsQuery query);

  Optional<Concert> handle(GetConcertByIdQuery query);

  List<Concert> handle(GetConcertsByGenreQuery query);

  List<Concert> handle(GetConcertsByArtistQuery query);

  List<Concert> handle(GetAllConcertsAttendedByUserIdQuery query);
}
