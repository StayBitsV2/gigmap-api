package com.staybits.gigmapapi.concerts.domain.model.queries;

import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;

public record GetConcertsByGenreQuery(Genre genre) {
}
