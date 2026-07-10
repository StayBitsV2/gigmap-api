package com.staybits.gigmapapi.analytics.interfaces.rest;

import com.staybits.gigmapapi.analytics.domain.model.aggregates.AnalyticsEvent;
import com.staybits.gigmapapi.analytics.domain.model.valueobjects.AnalyticsEventType;
import com.staybits.gigmapapi.analytics.infrastructure.persistence.jpa.repositories.AnalyticsEventRepository;
import com.staybits.gigmapapi.analytics.interfaces.rest.resources.CreateAnalyticsEventResource;
import com.staybits.gigmapapi.analytics.interfaces.rest.resources.AnalyticsEventResource;
import com.staybits.gigmapapi.analytics.interfaces.rest.transform.AnalyticsEventResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/analytics", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Analytics", description = "Analytics event tracking endpoints")
public class AnalyticsController {

    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsController(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @PostMapping("/events")
    @Operation(summary = "Register analytics event", description = "Register a new analytics event for experimental KPIs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid event type or request body")
    })
    public ResponseEntity<AnalyticsEventResource> createEvent(@RequestBody CreateAnalyticsEventResource resource) {
        try {
            var eventType = AnalyticsEventType.valueOf(resource.eventType().toUpperCase());
            var event = new AnalyticsEvent(eventType, resource.userId(), resource.metadata());
            var saved = analyticsEventRepository.save(event);
            var eventResource = AnalyticsEventResourceFromEntityAssembler.toResourceFromEntity(saved);
            return new ResponseEntity<>(eventResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
