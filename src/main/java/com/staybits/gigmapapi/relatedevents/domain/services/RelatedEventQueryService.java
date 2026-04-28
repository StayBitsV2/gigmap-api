package com.staybits.gigmapapi.relatedevents.domain.services;

import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventByIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventsByConcertIdQuery;

import java.util.List;
import java.util.Optional;

public interface RelatedEventQueryService {
    Optional<RelatedEvent> handle(GetRelatedEventByIdQuery query);
    
    List<RelatedEvent> handle(GetRelatedEventsByConcertIdQuery query);
}
