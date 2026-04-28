package com.staybits.gigmapapi.relatedevents.application.internal.queryservices;

import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventByIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.model.queries.GetRelatedEventsByConcertIdQuery;
import com.staybits.gigmapapi.relatedevents.domain.services.RelatedEventQueryService;
import com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories.RelatedEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelatedEventQueryServiceImpl implements RelatedEventQueryService {

    private final RelatedEventRepository relatedEventRepository;

    public RelatedEventQueryServiceImpl(RelatedEventRepository relatedEventRepository) {
        this.relatedEventRepository = relatedEventRepository;
    }

    @Override
    public Optional<RelatedEvent> handle(GetRelatedEventByIdQuery query) {
        return relatedEventRepository.findById(query.id());
    }

    @Override
    public List<RelatedEvent> handle(GetRelatedEventsByConcertIdQuery query) {
        return relatedEventRepository.findByConcertId(query.concertId());
    }
}
